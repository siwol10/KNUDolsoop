package com.example.SpringBoot.urgent.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UrgentReservationDTO {
    private Long resNum;
    private LocalDate careDate;
    private String category;
    private Long formNum;
    private Long memNum;
}