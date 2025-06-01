package com.example.SpringBoot.careForm.dto;

import lombok.Data;

@Data
public class CareFormDTO {
    private Long memNum;
    private String title;
    private String careType;
    private String careDay;
    private String careStartTime;
    private String careEndTime;
    private String region;
    private String pay;
    private String request;
}