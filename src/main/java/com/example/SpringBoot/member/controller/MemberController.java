package com.example.SpringBoot.member.controller;

import com.example.SpringBoot.member.dto.MemberDTO;
import com.example.SpringBoot.member.service.MemberService;
import com.example.SpringBoot.member.service.MessageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MessageService messageService;


    @GetMapping("/signUpPick")
    public String signUpPick() {
        return "signUpPick";
    }

    @GetMapping("/signUpParents")
    public String signUpParents(Model model) {
        List<String> sidoList = List.of("서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시","대전광역시","울산광역시","세종시","경기도","강원도","충청북도","충청남도","전라북도","전라남도","경상북도","경상남도","제주도"); // 등등
        model.addAttribute("sidoList", sidoList);
        return "signUpParents";
    }

    @GetMapping("/signUpSitter")
    public String signUpSitter(Model model) {
        List<String> sidoList = List.of("서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시","대전광역시","울산광역시","세종시","경기도","강원도","충청북도","충청남도","전라북도","전라남도","경상북도","경상남도","제주도"); // 등등
        model.addAttribute("sidoList", sidoList);
        return "signUpSitter";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/signUpSitter")
    public String processSitterSignup(@ModelAttribute MemberDTO memberDTO,
                                      @RequestParam(name = "agreement", defaultValue = "false") boolean agreement) {
        memberDTO.setMemberType("SITTER");
        messageService.registerMember(memberDTO, agreement);
        return "redirect:/login";
    }

    @PostMapping("/signUpParents")
    public String processParentSignup(@ModelAttribute MemberDTO memberDTO) {
        memberDTO.setMemberType("PARENTS");
        messageService.registerMember(memberDTO, false);
        return "redirect:/login";
    }


    @PostMapping("/login")
    public String login(@RequestParam String id,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        MemberDTO loginMember = memberService.login(id, password);

        if (loginMember != null) {
            session.setAttribute("loginMember", loginMember);
            model.addAttribute("message",loginMember.getName()+"님 환영합니다.");
            model.addAttribute("searchUrl","/myPage");
            return "message";
        } else {
            model.addAttribute("loginError", "아이디 또는 비밀번호가 틀렸습니다.");
            model.addAttribute("message","아이디 또는 비밀번호가 틀렸습니다.");
            model.addAttribute("searchUrl","/login");
            return "message";
        }
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/myPage/member/edit")
    public String editMember(@RequestParam(name = "mem") Long memNum, @RequestParam int type, Model model) {
        MemberDTO memberDTO = memberService.findByMemNum(memNum.toString());

        boolean agreement = false;
        if(type == 1)
            agreement = messageService.findByMemNum(memNum);

        List<String> sidoList = List.of("서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시","대전광역시","울산광역시","세종시","경기도","강원도","충청북도","충청남도","전라북도","전라남도","경상북도","경상남도","제주도"); // 등등
        model.addAttribute("sidoList", sidoList);
        model.addAttribute("member", memberDTO);
        model.addAttribute("edit", true);

        if(type == 1) {
            model.addAttribute("agreement", agreement);
            return "signUpSitter";
        } else
            return "signUpParents";
    }

    @PostMapping("myPage/member/edit")
    public String updateMember(@ModelAttribute MemberDTO memberDTO, @RequestParam(name = "agreement", defaultValue = "false") boolean agreement, Model model) {
        messageService.updateMember(memberDTO, agreement);

        model.addAttribute("message", "수정이 완료되었습니다.");
        model.addAttribute("searchUrl","/myPage");
        return "message";
    }
}