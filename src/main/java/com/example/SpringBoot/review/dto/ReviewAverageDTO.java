package com.example.SpringBoot.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReviewAverageDTO {
    private Long memNum;
    private Float total;
    private Integer count;
    private Float average;
}