package com.example.SpringBoot.reservation.service;

import com.example.SpringBoot.member.repository.MemberRepository;
import com.example.SpringBoot.reservation.dto.ReservationDTO;
import com.example.SpringBoot.reservation.dto.ReservationDateDTO;
import com.example.SpringBoot.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;

    public List<ReservationDTO> findReservations(Long num, String part, int page, int pageSize) {
        // 내 예약 찾기 (새로운 예약 / 진행 중인 예약 / 완료된 예약 / 내가 신청한 예약)
        List<ReservationDTO> reservationList;
        int offset = (page - 1) * pageSize;

        if(part.equals("new")) {
            reservationList = reservationRepository.findByTargetNum(num, offset, page, pageSize);
        } else if(part.equals("ongoing")){
            reservationList = reservationRepository.findOngoing(num, offset, page, pageSize);
        } else if(part.equals("completed")) {
            reservationList = reservationRepository.findCompleted(num, offset, page, pageSize);
        } else if(part.equals("dropped")){
            reservationList = reservationRepository.findDropped(num, offset, page, pageSize);
        } else {
            reservationList = reservationRepository.findByMemNum(num, offset, page, pageSize);
        }

        // 예약 번호들
        List<Long> resNumList = reservationList.stream().map(ReservationDTO::getResNum).collect(Collectors.toList());

        if(resNumList.isEmpty())
            return reservationList;


        // ▼ 예약번호별로 예약 날짜 찾기 + 예약 번호와 날짜 매핑
        List<ReservationDateDTO> dateList = reservationRepository.findDate(resNumList);
        Map<Long, List<ReservationDateDTO>> date = dateList.stream().collect(Collectors.groupingBy(ReservationDateDTO::getResNum));

        for(ReservationDTO reservation : reservationList) {
            List<ReservationDateDTO> dates = date.getOrDefault(reservation.getResNum(), new ArrayList<>());
            reservation.setDate(dates);

            // 따로따로 저장되어 있던 날짜들을 하나의 문자열로 합치기
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd");
            String summary = dates.stream().map(d -> d.getResDate().format(formatter)).collect(Collectors.joining(", "));
            reservation.setDateSummary(summary);

            Long opponent = reservation.getMemNum();
            if(num == reservation.getMemNum()) {
                opponent = reservation.getTargetNum();
            }

            String memberName = memberRepository.findByMemNum(opponent.toString()).getName();
            reservation.setName(memberName);
        }

        return reservationList;
    }

    public int countReservations(Long num, String part) {
        int totalCount;

        if(part.equals("new")) {
            totalCount = reservationRepository.countByTargetNum(num);
        } else if(part.equals("ongoing")) {
            totalCount = reservationRepository.countOngoing(num);
        } else if(part.equals("completed")) {
            totalCount = reservationRepository.countCompleted(num);
        } else if(part.equals("dropped")){
            totalCount = reservationRepository.countDropped(num);
        } else {
            totalCount = reservationRepository.countByMemNum(num);
        }

        return totalCount;
    }

    public void updateState (Long resNum, String state) {
        reservationRepository.updateState(resNum, state);
    }

    // 날짜에 따라 예약 상태 변경 (예정 -> 진행 중 or 진행 중 -> 완료
    // or 날짜 지날 때까지 거절/승인 안 해준 예약 -> 종료)
    @Transactional
    public void updateStateByResDate() {
        List<ReservationDTO> reservationList = reservationRepository.findAll();
        if(reservationList.isEmpty())
            return;

        // 예약번호, 예약 날짜 매핑
        List<Long> resNumList = reservationList.stream().map(ReservationDTO::getResNum).collect(Collectors.toList());
        List<ReservationDateDTO> dateList = reservationRepository.findDate(resNumList);
        Map<Long, List<ReservationDateDTO>> date = dateList.stream().collect(Collectors.groupingBy(ReservationDateDTO::getResNum));

        LocalDate today = LocalDate.now();

        for(ReservationDTO reservation : reservationList) {
            List<ReservationDateDTO> dates = date.getOrDefault(reservation.getResNum(), new ArrayList<>());
            reservation.setDate(dates);

            boolean isPast = dates.stream().allMatch(d -> d.getResDate().isBefore(today));
            boolean isToday = dates.stream().anyMatch(d -> d.getResDate().isEqual(today));
            // inBetween: 오늘이 예약 날짜 사이에 껴있는지
            boolean inBetween = !isPast && !isToday &&
                    (dates.stream().anyMatch(d -> d.getResDate().isBefore(today)) &&
                            dates.stream().anyMatch(d -> d.getResDate().isAfter(today)));

            if(!reservation.getState().equals("대기") && isPast) { // 예약 날짜가 오늘을 지났으면 '완료'
                reservationRepository.updateState(reservation.getResNum(), "완료");
            } else if(!reservation.getState().equals("대기") && (isToday || inBetween)) { // 오늘이 포함되어 있으면 '진행 중'
                reservationRepository.updateState(reservation.getResNum(), "진행 중");
            } else if(reservation.getState().equals("대기") && isPast) {// 날짜가 지날 때까지 승인/거절 안 해준 예약 '종료'로 표시
                reservationRepository.updateState(reservation.getResNum(), "종료");
            }
        }
    }

    public void updateReview(Long resNum, String user) {
        if(user.equals("mem"))
            reservationRepository.updateMemReview(resNum);
        else
            reservationRepository.updateTargetReview(resNum);
    }

    public ReservationDTO findByResNum(Long resNum) {
        return reservationRepository.findByResNum(resNum);
    }
}
