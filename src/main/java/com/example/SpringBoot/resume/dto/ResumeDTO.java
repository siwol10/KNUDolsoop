package com.example.SpringBoot.resume.dto;

import lombok.Data;

@Data
public class ResumeDTO {
    private Long memNum;
    private String name;
    private int age;
    private String phoneNumber;
    private String region;
    private String careType;
    private String careDay;
    private String careStartTime;
    private String careEndTime;
    private String career;
    private String introduction;
    private String certificate;
    private String photo;
    private String certificateText;

    private String photoUrl; // file_object의 url을 담을 필드

}

