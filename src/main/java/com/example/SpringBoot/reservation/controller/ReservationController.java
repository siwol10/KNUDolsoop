package com.example.SpringBoot.reservation.controller;

import com.example.SpringBoot.member.dto.MemberDTO;
import com.example.SpringBoot.reservation.dto.ReservationDTO;
import com.example.SpringBoot.reservation.service.ReservationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    // 새로운 예약 확인
    @GetMapping("/myPage/reservation/new")
    public String newReservation(@RequestParam(name = "page", defaultValue = "1") int page, HttpSession session, Model model) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");

        if(loginMember == null) {
            model.addAttribute("message","로그인이 필요한 서비스 입니다.");
            model.addAttribute("searchUrl","/login");
            return "message";
        }

        int pageSize = 5;
        List<ReservationDTO> reservationList = reservationService.findReservations(loginMember.getMemNum(), "new", page, pageSize);

        int totalCount = reservationService.countReservations(loginMember.getMemNum(), "new");
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        int blockSize = 5;
        int currentBlock = (page - 1) / blockSize;
        int startPage = currentBlock * blockSize + 1;
        int endPage = Math.min(startPage + blockSize - 1, totalPages);

        model.addAttribute("reservationList", reservationList);
        model.addAttribute("memType", loginMember.getMemberType());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "reservationListNew";
    }

    // 예약 처리 (확인, 거절, 취소)
    @PostMapping("/myPage/reservation")
    public String updateState(@RequestParam Long resNum, @RequestParam String state, @RequestParam int part, HttpSession session, Model model) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");

        ReservationDTO reservationDTO = reservationService.findByResNum(resNum);
        Long memNum = loginMember.getMemNum();

        if((memNum != reservationDTO.getMemNum()) && (memNum != reservationDTO.getTargetNum())) {
            model.addAttribute("message","다시 시도해주세요.");
            if(part == 1)
                model.addAttribute("searchUrl","/myPage/reservation/new");
            else
                model.addAttribute("searchUrl","/myPage/reservation/applied");
            return "message";
        }

        reservationService.updateState(resNum, state);

        if (part == 1)
            return "redirect:/myPage/reservation/new";
        else
            return "redirect:/myPage/reservation/applied";
    }

    // 진행 중인 예약
    @GetMapping("/myPage/reservation/ongoing")
    public String ongoingReservation(@RequestParam(name = "page", defaultValue = "1") int page, HttpSession session, Model model) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");

        if(loginMember == null) {
            model.addAttribute("message","로그인이 필요한 서비스 입니다.");
            model.addAttribute("searchUrl","/login");
            return "message";
        }

        int pageSize = 5;
        List<ReservationDTO> reservationList = reservationService.findReservations(loginMember.getMemNum(), "ongoing", page, pageSize);

        int totalCount = reservationService.countReservations(loginMember.getMemNum(), "ongoing");
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        int blockSize = 5;
        int currentBlock = (page - 1) / blockSize;
        int startPage = currentBlock * blockSize + 1;
        int endPage = Math.min(startPage + blockSize - 1, totalPages);

        model.addAttribute("reservationList", reservationList);
        model.addAttribute("memType", loginMember.getMemberType());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "reservationListOngoing";
    }

    // 완료된 예약
    @GetMapping("/myPage/reservation/completed")
    public String completedReservation(@RequestParam(name = "page", defaultValue = "1") int page, HttpSession session, Model model) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");

        if(loginMember == null) {
            model.addAttribute("message","로그인이 필요한 서비스 입니다.");
            model.addAttribute("searchUrl","/login");
            return "message";
        }

        int pageSize = 5;
        List<ReservationDTO> reservationList = reservationService.findReservations(loginMember.getMemNum(), "completed", page, pageSize);

        int totalCount = reservationService.countReservations(loginMember.getMemNum(), "completed");
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        int blockSize = 5;
        int currentBlock = (page - 1) / blockSize;
        int startPage = currentBlock * blockSize + 1;
        int endPage = Math.min(startPage + blockSize - 1, totalPages);

        model.addAttribute("reservationList", reservationList);
        model.addAttribute("memType", loginMember.getMemberType());
        model.addAttribute("num", loginMember.getMemNum());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "reservationListFinish";
    }

    // 진행되지 못한 예약(거절, 취소, 종료)
    @GetMapping("/myPage/reservation/dropped")
    public String droppedReservation(@RequestParam(name = "page", defaultValue = "1") int page, HttpSession session, Model model) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");

        if(loginMember == null) {
            model.addAttribute("message","로그인이 필요한 서비스 입니다.");
            model.addAttribute("searchUrl","/login");
            return "message";
        }

        int pageSize = 5;
        List<ReservationDTO> reservationList = reservationService.findReservations(loginMember.getMemNum(), "dropped", page, pageSize);

        int totalCount = reservationService.countReservations(loginMember.getMemNum(), "dropped");
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        int blockSize = 5;
        int currentBlock = (page - 1) / blockSize;
        int startPage = currentBlock * blockSize + 1;
        int endPage = Math.min(startPage + blockSize - 1, totalPages);

        model.addAttribute("reservationList", reservationList);
        model.addAttribute("memNum", loginMember.getMemNum());
        model.addAttribute("memType", loginMember.getMemberType());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "reservationListDropped";
    }

    // 예약 신청 내역
    @GetMapping("/myPage/reservation/applied")
    public String appliedReservation(@RequestParam(name = "page", defaultValue = "1") int page, HttpSession session, Model model) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");

        if(loginMember == null) {
            model.addAttribute("message","로그인이 필요한 서비스 입니다.");
            model.addAttribute("searchUrl","/login");
            return "message";
        }

        int pageSize = 5;
        List<ReservationDTO> reservationList = reservationService.findReservations(loginMember.getMemNum(), "application", page, pageSize);

        int totalCount = reservationService.countReservations(loginMember.getMemNum(), "application");
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        int blockSize = 5;
        int currentBlock = (page - 1) / blockSize;
        int startPage = currentBlock * blockSize + 1;
        int endPage = Math.min(startPage + blockSize - 1, totalPages);

        model.addAttribute("memType", loginMember.getMemberType());
        model.addAttribute("reservationList", reservationList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "reservationListApplied";
    }
}
