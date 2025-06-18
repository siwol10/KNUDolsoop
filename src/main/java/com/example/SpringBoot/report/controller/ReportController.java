package com.example.SpringBoot.report.controller;

import com.example.SpringBoot.member.dto.MemberDTO;
import com.example.SpringBoot.report.dto.ReportDTO;
import com.example.SpringBoot.report.service.ReportService;
import com.example.SpringBoot.reservation.dto.ReservationDTO;
import com.example.SpringBoot.reservation.service.ReservationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final ReservationService reservationService;

    // 알림장 작성 화면
    @GetMapping("/myPage/reservation/report")
    public String report(@ModelAttribute("report") ReportDTO reportDTO, @RequestParam(name = "res") Long resNum, HttpSession session, Model model) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");

        if(loginMember == null) {
            model.addAttribute("message","로그인이 필요한 서비스 입니다.");
            model.addAttribute("searchUrl","/login");
            return "message";
        }

        ReservationDTO reservationDTO = reservationService.findByResNum(resNum);
        Long memNum = loginMember.getMemNum();

        if((memNum != reservationDTO.getMemNum()) && (memNum != reservationDTO.getTargetNum())) {
            model.addAttribute("message","다시 시도해주세요.");
            model.addAttribute("searchUrl","/myPage/reservation/ongoing");
            return "message";
        }

        model.addAttribute("resNum", resNum);
        if(!model.containsAttribute("report"))
            model.addAttribute("report", new ReportDTO());
        return "report";
    }

    // 알림장 저장
    @PostMapping("/myPage/reservation/report/save")
    public String saveReport(ReportDTO reportDTO, Model model, RedirectAttributes redirectAttributes) {
        try {
            reportService.saveReport(reportDTO);
            model.addAttribute("message","저장이 완료되었습니다.");
            return "messageAndClose";
        } catch(DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "해당 날짜에 이미 저장한 내역이 있습니다.");
            redirectAttributes.addFlashAttribute("report", reportDTO);
            return "redirect:/myPage/reservation/report?res=" + reportDTO.getResNum();
        }
    }

    // 알림장 확인
    @GetMapping("/myPage/reservation/report/view")
    public String reportView(@RequestParam(name = "res") Long resNum, @RequestParam(name = "report", required = false) Long reportNum, HttpSession session, Model model) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");

        if(loginMember == null) {
            model.addAttribute("message","로그인이 필요한 서비스 입니다.");
            model.addAttribute("searchUrl","/login");
            return "message";
        }

        ReservationDTO reservationDTO = reservationService.findByResNum(resNum);
        Long memNum = loginMember.getMemNum();

        if((memNum != reservationDTO.getMemNum()) && (memNum != reservationDTO.getTargetNum())) {
            model.addAttribute("message","다시 시도해주세요.");
            model.addAttribute("searchUrl","/myPage/reservation/ongoing");
            return "message";
        }

        ReportDTO reportDTO;
        if(reportNum == null) {
            reportDTO = reportService.findFirstReport(resNum);
        } else {
            reportDTO = reportService.findByReportNum(reportNum);
        }

        if(reportDTO == null) {
            model.addAttribute("message","작성된 알림장이 없습니다.");
            return "messageAndClose";
        }

        Long preNum = reportService.findPreNum(resNum, reportDTO.getCareDate()); // 이전 알림장
        Long nextNum = reportService.findNextNum(resNum, reportDTO.getCareDate()); // 다음 알림장

        model.addAttribute("report", reportDTO);
        model.addAttribute("resNum", resNum);
        model.addAttribute("preNum", preNum);
        model.addAttribute("nextNum", nextNum);

        return "reportView";
    }
}
