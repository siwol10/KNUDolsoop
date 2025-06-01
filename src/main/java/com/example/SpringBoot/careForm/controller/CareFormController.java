package com.example.SpringBoot.careForm.controller;

import com.example.SpringBoot.careForm.dto.CareFormDTO;
import com.example.SpringBoot.careForm.service.CareFormService;
import com.example.SpringBoot.member.dto.MemberDTO;
import com.example.SpringBoot.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class CareFormController {
    private final CareFormService careFormService;
    private final MemberService memberService;

    @GetMapping("/careForm")
    public String careForm(Model model, HttpSession session) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        MemberDTO member = memberService.findById(loginMember.getId());
        model.addAttribute("member",member);
        if("SITTER".equals(member.getMemberType())) {
            model.addAttribute("message","보호자 사용자만 이용 가능합니다.");
            model.addAttribute("searchUrl","/");
            return "message";
        }else if(loginMember == null) {
            model.addAttribute("message", "로그인이 필요한 서비스 입니다.");
            model.addAttribute("searchUrl", "/login");
            return "message";
        }

        List<String> timeOptions = new ArrayList<>();
        for (int hour = 0; hour <= 23; hour++) {
            timeOptions.add(String.format("%02d:00", hour));
        }
        model.addAttribute("timeOptions", timeOptions);

        return "careForm";
    }


    @PostMapping("/careForm/save")
    public String submitForm(@ModelAttribute CareFormDTO careFormDTO,
                             @RequestParam("careDays") String[] careDays,
                             @RequestParam("careStartTime") String careStartTime,
                             @RequestParam("careEndTime") String careEndTime,
                             HttpSession session) throws IOException {

        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        Long memNum = loginMember.getMemNum();
        careFormDTO.setMemNum(memNum);
        careFormDTO.setCareDay(String.join(",", careDays));
        careFormDTO.setCareStartTime(careStartTime);
        careFormDTO.setCareEndTime(careEndTime);


        careFormService.save(careFormDTO);
        return "redirect:/test";
    }

    @GetMapping("/careFormDetail")
    public String careFormDetail(Model model, HttpSession session) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        MemberDTO member = memberService.findById(loginMember.getId());
        model.addAttribute("member",member);
        if("SITTER".equals(member.getMemberType())) {
            model.addAttribute("message","보호자 사용자만 이용 가능합니다.");
            model.addAttribute("searchUrl","/");
            return "message";
        }

        Long memNum = member.getMemNum();
        CareFormDTO careForm = careFormService.findByMemNum(memNum);
        model.addAttribute("careForm", careForm);

        return "careFormDetail";
    }

//    @GetMapping("/parentsBoard")
//    public String parentsBoard(Model model) {
//        model.addAttribute("careFormList", careFormService.findAll());
//        return "parentsBoard";
//    }

}
