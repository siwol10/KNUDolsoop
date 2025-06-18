package com.example.SpringBoot.report.repository;

import com.example.SpringBoot.report.dto.ReportDTO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ReportRepository {
    private final SqlSessionTemplate sql;

    public void saveReport(ReportDTO reportDTO) {
        sql.insert("report.saveReport", reportDTO);
    }

    public ReportDTO findFirstReport(Long resNum) {
        return sql.selectOne("report.findFirstReport",resNum);
    }

    public ReportDTO findByReportNum(Long reportNum) {
        return sql.selectOne("report.findByReportNum", reportNum);
    }

    public Long findPreNum(Long resNum, LocalDate careDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("resNum", resNum);
        params.put("careDate", careDate);

        return sql.selectOne("report.findPreNum", params);
    }

    public Long findNextNum(Long resNum, LocalDate careDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("resNum", resNum);
        params.put("careDate", careDate);

        return sql.selectOne("report.findNextNum", params);
    }
}
