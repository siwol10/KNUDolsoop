package com.example.SpringBoot.resume.controller;

import com.example.SpringBoot.file.dto.FileObjectDTO;
import com.example.SpringBoot.file.service.FileObjectService;
import com.example.SpringBoot.member.dto.MemberDTO;
import com.example.SpringBoot.member.service.MemberService;
import com.example.SpringBoot.reservation.dto.ReservationDTO;
import com.example.SpringBoot.reservation.dto.ReservationDateDTO;
import com.example.SpringBoot.resume.dto.ResumeDTO;
import com.example.SpringBoot.resume.dto.ReviewForResumeDTO;
import com.example.SpringBoot.resume.service.ResumeService;
import com.example.SpringBoot.review.dto.ReviewAverageNoArgsDTO;
import com.example.SpringBoot.s3.S3Uploader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;
    private final MemberService memberService;
    private final FileObjectService fileObjectService;
    private final S3Uploader s3Uploader;

    @GetMapping("/resume")
    public String resume(Model model, HttpSession session) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        System.out.println("test");
        if(loginMember == null) {
            System.out.println("test2");
            model.addAttribute("message","로그인이 필요한 서비스 입니다.");
            model.addAttribute("searchUrl","/login");
            return "message";
        }
        MemberDTO member = memberService.findById(loginMember.getId());
        model.addAttribute("member",member);
        if("PARENTS".equals(loginMember.getMemberType())){
            model.addAttribute("message","돌보미 사용자만 이용 가능합니다.");
            model.addAttribute("searchUrl","/login");
            return "message";
        }
        List<String> timeOptions = new ArrayList<>();
        for (int hour = 0; hour <= 23; hour++) {
            timeOptions.add(String.format("%02d:00", hour));
            timeOptions.add(String.format("%02d:30", hour));
        }
        model.addAttribute("timeOptions", timeOptions);


        return "resume";
    }

    @PostMapping("/resume/save")
    public String submitForm(@ModelAttribute ResumeDTO resumeDTO,
                             @RequestParam("careDays") String[] careDays,
                             @RequestParam("careStartTime") String careStartTime,
                             @RequestParam("careEndTime") String careEndTime,
                             @RequestParam("certificateFiles") MultipartFile[] certificateFiles,
                             @RequestParam("photoFile") MultipartFile photoFile,
                             HttpSession session) throws IOException {

        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        Long memNum = loginMember.getMemNum();
        resumeDTO.setMemNum(memNum);
        resumeDTO.setCareDay(String.join(",", careDays));
        resumeDTO.setCareStartTime(careStartTime);
        resumeDTO.setCareEndTime(careEndTime);

        if (certificateFiles != null && certificateFiles.length > 0) {
            s3Uploader.uploadAndRegisterFiles("resume", memNum, "certificate", certificateFiles);
        }
        if (!photoFile.isEmpty()) {
            s3Uploader.uploadAndRegisterFile("resume", memNum, "photo", photoFile);
        }


        resumeService.save(resumeDTO);
        return "redirect:/";
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
    public String resumeDetail(Model model, @RequestParam String memNum, @RequestParam(required = false) String type, HttpSession session) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        if(loginMember == null) {
            model.addAttribute("message","로그인이 필요한 서비스 입니다.");
            model.addAttribute("searchUrl","/login");
            return "message";
        }

        MemberDTO member = memberService.findById(loginMember.getId());
        model.addAttribute("member",member);

        List<FileObjectDTO> certificateFiles = fileObjectService.findByReference("resume", member.getMemNum(), "certificate");
        model.addAttribute("certificateFiles", certificateFiles);
        for (FileObjectDTO certificateFile : certificateFiles) {
            System.out.println(certificateFile);
        }

//        if("PARENTS".equals(loginMember.getMemberType())){
//            model.addAttribute("message","보호자 사용자만 이용 가능합니다.");
//            model.addAttribute("searchUrl","/login");
//            return "message";
//        }

        ResumeDTO resume = resumeService.findByMemNum(Long.parseLong(memNum));
        model.addAttribute("resume", resume);
        List<ReviewForResumeDTO> reviewList = resumeService.findReviewByTargetNum(memNum);
        for (ReviewForResumeDTO reviewForResumeDTO : reviewList) {
            System.out.println(reviewForResumeDTO.toString());
        }
        model.addAttribute("resume", resume);
        model.addAttribute("reviewList", reviewList);

        if((type != null) && (resume == null)) {
            model.addAttribute("message","이력서를 작성하지 않은 사용자입니다.");
            return "message";
        }

        if(type != null) {
            model.addAttribute("type", "view");
        }

        return "resumeDetail";
    }

    @GetMapping("/sitterBoard")
    public String sitterBoard(Model model,
                              @RequestParam(required = false) String careType,
                              @RequestParam(required = false) List<String> day,
                              @RequestParam(required = false) String startTime,
                              @RequestParam(required = false) String endTime,
                              @RequestParam(required = false) String region,
                              @RequestParam(required = false) String sido,
                              @RequestParam(required = false) String gugun,
                              @RequestParam(required = false) String dong,
                              @RequestParam(required = false) String hasCertificate

    ) {
        model.addAttribute("careType", careType);
        model.addAttribute("days", day != null ? day : new ArrayList<>());
        model.addAttribute("startTime", startTime);
        model.addAttribute("endTime", endTime);
        model.addAttribute("region", region);
        model.addAttribute("hasCertificate", hasCertificate); // 잊지 말기!

        Map<String, Object> filters = new HashMap<>();
        filters.put("careType", careType);
        filters.put("days", day);
        filters.put("startTime", startTime);
        filters.put("endTime", endTime);
        filters.put("region", region);
        filters.put("hasCertificate", hasCertificate);
        System.out.println("sido : " + sido);

        List<ResumeDTO> resumeFormList = resumeService.findByFilters(filters);
        model.addAttribute("resumeFormList", resumeFormList);
        // region 파싱해서 시/도/구/군/동 값 저장 (필터 유지용)
        model.addAttribute("sidoSelected", sido != null ? sido : "");
        model.addAttribute("gugunSelected", gugun != null ? gugun : "");
        model.addAttribute("dongSelected", dong != null ? dong : "");
        List<String> timeOptions = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            timeOptions.add(String.format("%02d:00", hour));
            timeOptions.add(String.format("%02d:30", hour));
        }
        model.addAttribute("timeOptions", timeOptions);
        List<String> sidoList = List.of("서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시","대전광역시","울산광역시","세종시","경기도","강원도","충청북도","충청남도","전라북도","전라남도","경상북도","경상남도","제주도"); // 등등
        model.addAttribute("sidoList", sidoList);
        List<String> daysOfWeek = List.of("월", "화", "수", "목", "금", "토", "일");
        model.addAttribute("daysOfWeek", daysOfWeek);

        //별점 평균
        List<ReviewAverageNoArgsDTO> averages = resumeService.findAverageStarsForAllSitters();
        Map<Long, Float> averageStarMap = averages.stream().collect(Collectors.toMap(ReviewAverageNoArgsDTO::getMemNum, ReviewAverageNoArgsDTO::getAverage));
        model.addAttribute("averageStarMap", averageStarMap);


        return "sitterBoard";
    }

    //예약하기
    @GetMapping("/resumeDetail/reserve")
    public String showCalendar(@RequestParam("memNum") Long memNum, Model model) throws JsonProcessingException {
        ResumeDTO resume = resumeService.getResumeByMemNum(memNum);

        // careDay 예시: "월,수,금"
        String careDay = resume.getCareDay();

        List<Integer> allowedDays = convertCareDayToDayOfWeek(careDay); // → [1,3,5]
        model.addAttribute("allowedDays", new ObjectMapper().writeValueAsString(allowedDays));  // JS에서 JSON으로 사용
        model.addAttribute("resume", resume);

        return "reserveSitter";
    }

    private List<Integer> convertCareDayToDayOfWeek(String careDay) {
        Map<String, Integer> dayMap = Map.of(
                "일", 0, "월", 1, "화", 2, "수", 3,
                "목", 4, "금", 5, "토", 6
        );
        return Arrays.stream(careDay.split(","))
                .map(String::trim)
                .map(dayMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @PostMapping("/resumeDetail/reserve")
    public String saveReservation(@RequestParam("selectedDates") String selectedDates,
                                  @RequestParam(value = "content", required = false) String content,
                                  @RequestParam("targetNum") Long targetNum,
                                  HttpSession session) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        ReservationDTO reservation = new ReservationDTO();
        reservation.setMemNum(loginMember.getMemNum());
        reservation.setTargetNum(targetNum);
        reservation.setContent(content != null ? content : "");
        reservation.setState("대기");
        reservation.setChoiceType("SITTER");

        List<ReservationDateDTO> dateList = Arrays.stream(selectedDates.split(","))
                .map(LocalDate::parse)
                .map(date -> {
                    ReservationDateDTO dto = new ReservationDateDTO();
                    dto.setResDate(date);
                    return dto;
                }).toList();

        reservation.setDate(dateList);
        resumeService.saveReservation(reservation); // resumeService에 위임

        return "redirect:/sitterBoard";
    }

    @GetMapping("myPage/resume/edit")
    public String editResume(HttpSession session, Model model) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        if(loginMember == null) {
            model.addAttribute("message","로그인이 필요한 서비스 입니다.");
            model.addAttribute("searchUrl","/login");
            return "message";

        }

        List<FileObjectDTO> certificateFiles = fileObjectService.findByReference("resume", loginMember.getMemNum(), "certificate");
        model.addAttribute("certificateFiles", certificateFiles);

        List<FileObjectDTO> photoFiles = fileObjectService.findByReference("resume", loginMember.getMemNum(), "photo");
        if(!photoFiles.isEmpty()) {
            FileObjectDTO photo = photoFiles.get(0);
            model.addAttribute("photo", photo);
        }

        List<String> timeOptions = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            timeOptions.add(String.format("%02d:00", hour));
            timeOptions.add(String.format("%02d:30", hour));
        }
        model.addAttribute("timeOptions", timeOptions);

        ResumeDTO resumeDTO = resumeService.findByMemNum(loginMember.getMemNum());
        resumeDTO.setCareStartTime(resumeDTO.getCareStartTime().substring(0, 5));
        resumeDTO.setCareEndTime(resumeDTO.getCareEndTime().substring(0, 5));
        model.addAttribute("resume", resumeDTO);
        model.addAttribute("member", loginMember);

        return "resume";
    }

    @PostMapping("myPage/resume/edit")
    public String updateResume(ResumeDTO resumeDTO, @RequestParam("careDays") String[] careDays, @RequestParam("careStartTime") String careStartTime,
                               @RequestParam("careEndTime") String careEndTime, @RequestParam("certificateFiles") MultipartFile[] certificateFiles,
                               @RequestParam("photoFile") MultipartFile photoFile, HttpSession session, Model model) throws IOException {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        Long memNum = loginMember.getMemNum();
        resumeDTO.setMemNum(memNum);
        resumeDTO.setCareDay(String.join(",", careDays));
        resumeDTO.setCareStartTime(careStartTime);
        resumeDTO.setCareEndTime(careEndTime);

        if (certificateFiles != null && certificateFiles.length > 0) {
            s3Uploader.uploadAndRegisterFiles("resume", memNum, "certificate", certificateFiles);
        }
        if (!photoFile.isEmpty()) {
            s3Uploader.uploadAndRegisterFile("resume", memNum, "photo", photoFile);
        }


        resumeService.updateResume(resumeDTO);

        model.addAttribute("message", "수정이 완료되었습니다.");
        model.addAttribute("searchUrl","/myPage");
        return "message";
    }

    @PostMapping("myPage/resume/delete")
    public String deleteResume(@RequestParam Long memNum, Model model, HttpSession session) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        if(memNum != loginMember.getMemNum()) {
            model.addAttribute("message","다시 시도해주세요.");
            model.addAttribute("searchUrl","/myPage");
            return "message";
        }

        resumeService.deleteResume(memNum);
        fileObjectService.deleteFileByReference("resume", memNum, "certificate");
        fileObjectService.deleteFileByReference("resume", memNum, "photo");

        model.addAttribute("message", "이력서가 정상적으로 삭제되었습니다.");
        model.addAttribute("searchUrl","/myPage");
        return "message";
    }


}
