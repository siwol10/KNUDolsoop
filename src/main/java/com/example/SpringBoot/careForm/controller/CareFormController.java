package com.example.SpringBoot.careForm.controller;

import com.example.SpringBoot.careForm.dto.CareFormDTO;
import com.example.SpringBoot.careForm.dto.ReviewForCareFormDTO;
import com.example.SpringBoot.careForm.service.CareFormService;
import com.example.SpringBoot.member.dto.MemberDTO;
import com.example.SpringBoot.member.service.MemberService;
import com.example.SpringBoot.reservation.dto.ReservationDTO;
import com.example.SpringBoot.reservation.dto.ReservationDateDTO;
import com.example.SpringBoot.review.dto.ReviewAverageNoArgsDTO;
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

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class CareFormController {
    private final CareFormService careFormService;
    private final MemberService memberService;

    @GetMapping("/careForm")
    public String careForm(Model model, HttpSession session) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        if(loginMember == null) {
            model.addAttribute("message","로그인이 필요한 서비스 입니다.");
            model.addAttribute("searchUrl","/login");
            return "message";
        }
        MemberDTO member = memberService.findById(loginMember.getId());
        model.addAttribute("member",member);
        if("SITTER".equals(loginMember.getMemberType())){
            model.addAttribute("message","보호자 사용자만 이용 가능합니다.");
            model.addAttribute("searchUrl","/login");
            return "message";
        }
        List<String> timeOptions = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            timeOptions.add(String.format("%02d:00", hour));
            timeOptions.add(String.format("%02d:30", hour));
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
        return "redirect:/";
    }

    @GetMapping("/careFormDetail")
    public String careFormDetail(Model model, @RequestParam String memNum, @RequestParam(required = false) String type, HttpSession session) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        if(loginMember == null) {
            model.addAttribute("message","로그인이 필요한 서비스 입니다.");
            model.addAttribute("searchUrl","/login");
            return "message";

        }
        MemberDTO member = memberService.findById(loginMember.getId());
        model.addAttribute("member",member);
//        if("SITTER".equals(loginMember.getMemberType())){
//            model.addAttribute("message","보호자 사용자만 이용 가능합니다.");
//            model.addAttribute("searchUrl","/login");
//            return "message";
//        }

//        Long memNum = member.getMemNum();
        CareFormDTO careForm = careFormService.findByMemNum(Long.parseLong(memNum));
        model.addAttribute("careForm", careForm);

        List<ReviewForCareFormDTO> reviewList = careFormService.findReviewByTargetNum(memNum);
        for (ReviewForCareFormDTO reviewForCareFormDTO : reviewList) {
            System.out.println(reviewForCareFormDTO.toString());
        }
        model.addAttribute("careForm", careForm);
        model.addAttribute("reviewList", reviewList);

        if((type != null) && (careForm == null)) {
            model.addAttribute("message","신청서를 작성하지 않은 사용자입니다.");
            return "message";
        }

        if(type != null) {
            model.addAttribute("type", "view");
        }

        return "careFormDetail";
    }

    @GetMapping("/parentsBoard")
    public String parentsBoard(Model model,
                               @RequestParam(required = false) String careType,
                               @RequestParam(required = false) List<String> day,
                               @RequestParam(required = false) String startTime,
                               @RequestParam(required = false) String endTime,
                               @RequestParam(required = false) String region,
                               @RequestParam(required = false) String sido,
                               @RequestParam(required = false) String gugun,
                               @RequestParam(required = false) String dong
    ) {
        model.addAttribute("careType", careType);
        model.addAttribute("days", day != null ? day : new ArrayList<>());
        model.addAttribute("startTime", startTime);
        model.addAttribute("endTime", endTime);
        model.addAttribute("region", region);
        Map<String, Object> filters = new HashMap<>();
        filters.put("careType", careType);
        filters.put("days", day);
        filters.put("startTime", startTime);
        filters.put("endTime", endTime);
        filters.put("region", region);
        System.out.println("sido : " + sido);

        List<CareFormDTO> careFormList = careFormService.findByFilters(filters);
        model.addAttribute("careFormList", careFormList);
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
        List<ReviewAverageNoArgsDTO> averages = careFormService.findAverageStarsForAllSitters();
        Map<Long, Float> averageStarMap = averages.stream().collect(Collectors.toMap(ReviewAverageNoArgsDTO::getMemNum, ReviewAverageNoArgsDTO::getAverage));
        model.addAttribute("averageStarMap", averageStarMap);


        return "parentsBoard";
    }

    //예약하기
    @GetMapping("/careFormDetail/reserve")
    public String showCalendar(@RequestParam("memNum") Long memNum, Model model) throws JsonProcessingException {
        CareFormDTO careForm = careFormService.getByMemNum(memNum);
        String careDay = careForm.getCareDay();
        List<Integer> allowedDays = convertCareDayToDayOfWeek(careDay);
        model.addAttribute("allowedDays", new ObjectMapper().writeValueAsString(allowedDays));
        model.addAttribute("resume", careForm); // th:value에서 사용됨
        return "reserveParents";
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

    @PostMapping("/careFormDetail/reserve")
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
        reservation.setChoiceType("PARENTS"); // 구분을 위해 설정

        List<ReservationDateDTO> dateList = Arrays.stream(selectedDates.split(","))
                .map(LocalDate::parse)
                .map(date -> {
                    ReservationDateDTO dto = new ReservationDateDTO();
                    dto.setResDate(date);
                    return dto;
                }).toList();

        reservation.setDate(dateList);
        careFormService.saveReservation(reservation);

        return "redirect:/parentsBoard";
    }

    @GetMapping("myPage/careForm/edit")
    public String editCareForm(HttpSession session, Model model) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        if(loginMember == null) {
            model.addAttribute("message","로그인이 필요한 서비스 입니다.");
            model.addAttribute("searchUrl","/login");
            return "message";

        }

        List<String> timeOptions = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            timeOptions.add(String.format("%02d:00", hour));
            timeOptions.add(String.format("%02d:30", hour));
        }
        model.addAttribute("timeOptions", timeOptions);

        CareFormDTO careFormDTO = careFormService.findByMemNum(loginMember.getMemNum());
        careFormDTO.setCareStartTime(careFormDTO.getCareStartTime().substring(0, 5));
        careFormDTO.setCareEndTime(careFormDTO.getCareEndTime().substring(0, 5));
        model.addAttribute("form", careFormDTO);
        model.addAttribute("member", loginMember);

        return "careForm";
    }

    @PostMapping("myPage/careForm/edit")
    public String updateCareForm(CareFormDTO careFormDTO, @RequestParam("careDays") String[] careDays, @RequestParam("careStartTime") String careStartTime,
                                 @RequestParam("careEndTime") String careEndTime, HttpSession session, Model model) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        Long memNum = loginMember.getMemNum();
        careFormDTO.setMemNum(memNum);
        careFormDTO.setCareDay(String.join(",", careDays));
        careFormDTO.setCareStartTime(careStartTime);
        careFormDTO.setCareEndTime(careEndTime);

        careFormService.updateCareForm(careFormDTO);

        model.addAttribute("message", "수정이 완료되었습니다.");
        model.addAttribute("searchUrl","/myPage");
        return "message";
    }

    @PostMapping("myPage/careForm/delete")
    public String deleteCareForm(@RequestParam Long memNum, HttpSession session, Model model) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        if(memNum != loginMember.getMemNum()) {
            model.addAttribute("message","다시 시도해주세요.");
            model.addAttribute("searchUrl","/myPage");
            return "message";
        }

        careFormService.deleteCareForm(memNum);

        model.addAttribute("message", "신청서가 정상적으로 삭제되었습니다.");
        model.addAttribute("searchUrl","/myPage");
        return "message";
    }

}
