package com.example.SpringBoot.review.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewAverageNoArgsDTO {
    private Long memNum;
    private Float total;
    private Integer count;
    private Float average;
}