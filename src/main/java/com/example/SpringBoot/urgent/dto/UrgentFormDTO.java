package com.example.SpringBoot.urgent.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UrgentFormDTO {
    private Long num;
    private String category;
    private String region;
    private String location;
    private String careTime;
    private String phoneNumber;
    private String information;
    private LocalDate careDate;
    private String state;
    private Long memNum;
}