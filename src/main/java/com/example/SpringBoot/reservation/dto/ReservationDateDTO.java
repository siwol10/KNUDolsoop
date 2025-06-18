package com.example.SpringBoot.reservation.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReservationDateDTO {
    private Long num;
    private LocalDate resDate;
    private Long resNum;
}