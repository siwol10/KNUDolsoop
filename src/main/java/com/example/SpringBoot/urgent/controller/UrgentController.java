package com.example.SpringBoot.urgent.controller;

import com.example.SpringBoot.member.dto.MemberDTO;
import com.example.SpringBoot.resume.dto.ResumeDTO;
import com.example.SpringBoot.urgent.dto.UrgentFormDTO;
import com.example.SpringBoot.urgent.dto.UrgentReservationDTO;
import com.example.SpringBoot.urgent.service.UrgentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UrgentController {
    private final UrgentService urgentService;

    // 신청서 작성 화면
    @GetMapping("/urgent/form")
    public String urgentForm(Model model, HttpSession session) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");

        if(loginMember == null) {
            model.addAttribute("message","로그인이 필요한 서비스 입니다.");
            model.addAttribute("searchUrl","/login");
            return "message";

        } else if("SITTER".equals(loginMember.getMemberType())){
            model.addAttribute("message","보호자 사용자만 이용 가능합니다.");
            model.addAttribute("searchUrl","/urgent");
            return "message";

        }

        String region = loginMember.getRegion();
        model.addAttribute("type", "input");
        model.addAttribute("region", region);

        return "urgentForm";
    }

    // 신청서 저장
    @PostMapping("/urgent/form")
    public String saveForm(UrgentFormDTO urgentFormDTO, HttpSession session) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        urgentFormDTO.setMemNum(loginMember.getMemNum());
        String region = urgentFormDTO.getRegion();
        urgentService.saveForm(urgentFormDTO, region);

        return "redirect:/urgent"; // 추후에 작성 완료 화면 넣기
    }

    // 신청서 작성 목록 (보호자)
    @GetMapping("/urgent/myForms")
    public String findMyForms(@RequestParam(value = "page", defaultValue = "1") int page, HttpSession session, Model model) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");

        if(loginMember == null) {
            model.addAttribute("message","로그인이 필요한 서비스 입니다.");
            model.addAttribute("searchUrl","/login");
            return "message";
        }

        Long memNum = loginMember.getMemNum();

        int pageSize = 20;

        List<UrgentFormDTO> formList = urgentService.findMyForms(memNum, page, pageSize);
        int totalCount = urgentService.countMyForms(memNum);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        model.addAttribute("formList", formList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("today", LocalDate.now());


        return "myUrgentForms";
    }

    // 목록 -> 신청서 확인 -> 신청서 화면
    @GetMapping("/urgent/form/view/{formNum}")
    public String formView(@PathVariable Long formNum, HttpSession session, Model model) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");

        if(loginMember == null) {
            model.addAttribute("message","로그인이 필요한 서비스 입니다.");
            model.addAttribute("searchUrl","/login");
            return "message";
        }

        UrgentFormDTO urgentFormDTO = urgentService.formView(formNum);

        if(!loginMember.getMemNum().equals(urgentFormDTO.getMemNum())){
            return "redirect:/";
        }

        String memType = loginMember.getMemberType();

        model.addAttribute("form", urgentFormDTO);
        model.addAttribute("type", memType);

        return "urgentForm";
    }

    // 신청서 삭제 (보호자)
    @PostMapping("/urgent/form/view/delete")
    public String deleteForm(@RequestParam(value = "formNum") Long formNum) {
        urgentService.deleteForm(formNum);

        return "redirect:/urgent/myForms";
    }

    @GetMapping("/urgent/resume/view/{formNum}")
    public String resumeView(@PathVariable Long formNum, Model model) {
        ResumeDTO resumeDTO = urgentService.findResume(formNum);
        System.out.println(formNum);

        model.addAttribute("resume", resumeDTO);
        model.addAttribute("type", "view");

        return "resumeDetail";
    }

    // 예약 내역 확인 (돌보미)
    @GetMapping("/urgent/myReservations")
    public String findMyReservations(@RequestParam(value = "page", defaultValue = "1") int page, HttpSession session, Model model) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");

        if(loginMember == null) {
            model.addAttribute("message","로그인이 필요한 서비스 입니다.");
            model.addAttribute("searchUrl","/login");
            return "message";
        }

        Long memNum = loginMember.getMemNum();

        int pageSize = 20;

        List<UrgentReservationDTO> resList = urgentService.findMyReservations(memNum, page, pageSize);
        int totalCount = urgentService.countMyReservations(memNum);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        model.addAttribute("resList", resList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("today", LocalDate.now());

        return "myUrgentReservations";
    }

    // 긴급 돌봄 공고
    @GetMapping("/urgent")
    public String findAllForms(@RequestParam(value = "page", defaultValue = "1") int page,HttpSession session, Model model) {
        int pageSize = 30;
        int totalCount, totalPages;
        List<UrgentFormDTO> formList;


        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        if(loginMember == null) {
            formList = urgentService.findAllForms(page, pageSize);
            totalCount = urgentService.countAllForms();
            totalPages = (int) Math.ceil((double) totalCount / pageSize);

            model.addAttribute("memType", "no");
        } else {
            String memType = loginMember.getMemberType();
            String region = loginMember.getRegion();

            formList = urgentService.findFormsByRegion(region, page, pageSize);
            totalCount = urgentService.countFormsByRegion();
            totalPages = (int) Math.ceil((double) totalCount / pageSize);

            model.addAttribute("memType", memType);
        }

        model.addAttribute("formList", formList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "urgentBoard";
    }

    // 긴급 돌봄 예약
    @PostMapping("/urgent/reserve")
    public String reservation(UrgentReservationDTO urgentReservationDTO,
                              HttpSession session,
                              @RequestParam Long formNum,
                              @RequestParam LocalDate careDate,
                              @RequestParam String category,
                              RedirectAttributes redirectAttributes) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        Long memNum = loginMember.getMemNum();

        if(loginMember == null) {
            redirectAttributes.addFlashAttribute("error", "로그인이 필요한 서비스입니다.");

            return "redirect:/login";
        } else {
            if(loginMember.getMemberType().equals("PARENTS")) {
                redirectAttributes.addFlashAttribute("error", "돌보미만 지원 가능합니다.");
                return "redirect:/urgent";
            }
        }

        urgentReservationDTO.setMemNum(memNum);
        urgentReservationDTO.setFormNum(formNum);
        urgentReservationDTO.setCareDate(careDate);
        urgentReservationDTO.setCategory(category);

        urgentService.reservation(urgentReservationDTO,formNum);

        return "redirect:/urgent/myReservations";
    }
}