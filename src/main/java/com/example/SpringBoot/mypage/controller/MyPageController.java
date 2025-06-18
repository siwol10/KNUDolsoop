package com.example.SpringBoot.mypage.controller;

import com.example.SpringBoot.careForm.dto.CareFormDTO;
import com.example.SpringBoot.careForm.service.CareFormService;
import com.example.SpringBoot.member.dto.MemberDTO;
import com.example.SpringBoot.resume.dto.ResumeDTO;
import com.example.SpringBoot.resume.service.ResumeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MyPageController {
    private final ResumeService resumeService;
    private final CareFormService careFormService;

    @GetMapping("/myPage")
    public String myPage(HttpSession session, Model model) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");

        if(loginMember == null) {
            model.addAttribute("message","로그인이 필요한 서비스 입니다.");
            model.addAttribute("searchUrl","/login");
            return "message";
        }

        String memType = loginMember.getMemberType();
        Long memNum = loginMember.getMemNum();

        int type = 0;  // 보호자
        ResumeDTO resumeDTO = null;
        CareFormDTO careFormDTO = null;
        if("SITTER".equals(memType)) {
            type = 1; // 돌보미
            resumeDTO = resumeService.findByMemNum(memNum);
        } else {
            careFormDTO = careFormService.findByMemNum(memNum);
        }

        model.addAttribute("memNum", loginMember.getMemNum());
        model.addAttribute("memType", type);
        model.addAttribute("resume", resumeDTO);
        model.addAttribute("careForm", careFormDTO);

        return "myPage";
    }
}
