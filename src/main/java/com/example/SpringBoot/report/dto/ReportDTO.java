package com.example.SpringBoot.report.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReportDTO {
    private Long num;
    private LocalDate careDate;
    private String content;
    private Long resNum;
}