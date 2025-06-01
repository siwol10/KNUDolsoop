package com.example.SpringBoot.resume.controller;

import com.example.SpringBoot.member.dto.MemberDTO;
import com.example.SpringBoot.member.service.MemberService;
import com.example.SpringBoot.resume.dto.ResumeDTO;
import com.example.SpringBoot.resume.service.ResumeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;
    private final MemberService memberService;

    @GetMapping("/resume")
    public String resume(Model model, HttpSession session) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        MemberDTO member = memberService.findById(loginMember.getId());
        model.addAttribute("member",member);
        if("PARENTS".equals(member.getMemberType())) {
            model.addAttribute("message","돌보미 사용자만 이용 가능합니다.");
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


        return "resume";
    }

    @PostMapping("/resume/save")
    public String submitForm(@ModelAttribute ResumeDTO resumeDTO,
                             @RequestParam("careDays") String[] careDays,
                             @RequestParam("careStartTime") String careStartTime,
                             @RequestParam("careEndTime") String careEndTime,
                             @RequestParam("certificateFile") MultipartFile certificateFile,
                             @RequestParam("photoFile") MultipartFile photoFile,
                             HttpSession session) throws IOException {

        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        Long memNum = loginMember.getMemNum();
        resumeDTO.setMemNum(memNum);
        resumeDTO.setCareDay(String.join(",", careDays));
        resumeDTO.setCareStartTime(careStartTime);
        resumeDTO.setCareEndTime(careEndTime);

        String uploadDir = "C:/upload/";

        if (!certificateFile.isEmpty()) {
            String certPath = saveFile(certificateFile, uploadDir);
            resumeDTO.setCertificate(certPath);
        }

        if (!photoFile.isEmpty()) {
            String photoPath = saveFile(photoFile, uploadDir);
            resumeDTO.setPhoto(photoPath);
        }


        resumeService.save(resumeDTO);
        return "redirect:/test";
    }

    private String saveFile(MultipartFile file, String uploadDir) throws IOException {
        String originalName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String saveName = uuid + "_" + originalName;

        File saveFile = new File(uploadDir + saveName);
        file.transferTo(saveFile);

        return "/upload/" + saveName;
    }

    @GetMapping("/resumeDetail")
    public String resumeDetail(Model model, HttpSession session) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        MemberDTO member = memberService.findById(loginMember.getId());
        model.addAttribute("member",member);
        if("PARENTS".equals(member.getMemberType())) {
            model.addAttribute("message","돌보미 사용자만 이용 가능합니다.");
            model.addAttribute("searchUrl","/");
            return "message";
        }

        Long memNum = member.getMemNum();
        ResumeDTO resume = resumeService.findByMemNum(memNum);
        model.addAttribute("resume", resume);

        return "resumeDetail";
    }


}
