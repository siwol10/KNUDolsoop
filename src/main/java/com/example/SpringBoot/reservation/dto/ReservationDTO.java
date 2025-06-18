package com.example.SpringBoot.reservation.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReservationDTO {
    private Long resNum;
    private String state;
    private String choiceType;
    private String content;
    private Long memNum;
    private Long targetNum;
    private Boolean memReview;
    private Boolean targetReview;

    // 처리용 속성 (DB에는 없음)
    private List<ReservationDateDTO> date;
    private String dateSummary;
    private String name;
}
