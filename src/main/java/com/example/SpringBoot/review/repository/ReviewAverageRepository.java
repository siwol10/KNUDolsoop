package com.example.SpringBoot.review.repository;

import com.example.SpringBoot.review.dto.ReviewAverageDTO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ReviewAverageRepository {
    private final SqlSessionTemplate sql;

    public ReviewAverageDTO findByMemNum(Long num) {
        return sql.selectOne("reviewAverage.findByMemNum", num);
    }

    public void saveAverage(ReviewAverageDTO reviewAverageDTO) {
        sql.insert("reviewAverage.saveAverage", reviewAverageDTO);
    }

    public void updateAverage(Long num, Float total, int count, Float average) {
        Map<String, Object> params = new HashMap<>();
        params.put("num", num);
        params.put("total", total);
        params.put("count", count);
        params.put("average", average);

        sql.insert("reviewAverage.updateAverage", params);
    }
}