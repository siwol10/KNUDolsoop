package com.example.SpringBoot.review.controller;

import com.example.SpringBoot.member.dto.MemberDTO;
import com.example.SpringBoot.reservation.dto.ReservationDTO;
import com.example.SpringBoot.reservation.service.ReservationService;
import com.example.SpringBoot.review.dto.ReviewDTO;
import com.example.SpringBoot.review.service.ReservationReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ReviewController {
    private final ReservationService reservationService;
    private final ReservationReviewService reservationReviewService;

    // 리뷰 작성
    @GetMapping("/myPage/reservation/review")
    public String review(@RequestParam(name = "res")Long resNum, HttpSession session, Model model) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");

        if(loginMember == null) {
            model.addAttribute("message","로그인이 필요한 서비스 입니다.");
            model.addAttribute("searchUrl","/login");
            return "message";
        }

        ReservationDTO reservationDTO = reservationService.findByResNum(resNum);
        Long memNum = loginMember.getMemNum();

        if(!memNum.equals(reservationDTO.getMemNum()) && !memNum.equals(reservationDTO.getTargetNum())) {
            model.addAttribute("message","다시 시도해주세요.");
            model.addAttribute("searchUrl","/myPage/reservation/completed");
            return "message";
        }

        Long receiver = reservationDTO.getMemNum();
        if(memNum == reservationDTO.getMemNum())
            receiver = reservationDTO.getTargetNum();

        model.addAttribute("writer",loginMember.getMemNum());
        model.addAttribute("receiver", receiver);
        model.addAttribute("resNum", resNum);

        return "review";
    }

    // 리뷰 저장
    @PostMapping("/myPage/reservation/review/save")
    public String saveReview(HttpSession session, ReviewDTO reviewDTO, Model model) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        Long memNum = loginMember.getMemNum();
        ReservationDTO reservationDTO = reservationService.findByResNum(reviewDTO.getResNum());

        String user = "mem";
        if(memNum == reservationDTO.getTargetNum())
            user = "target";

        reservationReviewService.saveReview(reviewDTO, user);

        model.addAttribute("message","리뷰 작성이 완료되었습니다.");
        model.addAttribute("searchUrl","/myPage/reservation/completed");
        return "message";
    }
}
