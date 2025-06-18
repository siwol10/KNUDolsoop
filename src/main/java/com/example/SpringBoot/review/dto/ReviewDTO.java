package com.example.SpringBoot.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    private Long num;
    private Integer star;
    private String Comment;
    private String detail;
    private Long resNum;
    private Long writer;
    private Long receiver;
}
