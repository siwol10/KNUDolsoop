package com.example.SpringBoot.review.repository;

import com.example.SpringBoot.review.dto.ReviewDTO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewRepository {
    private final SqlSessionTemplate sql;

    public void saveReview(ReviewDTO reviewDTO) {
        sql.insert("review.saveReview", reviewDTO);
    }
}