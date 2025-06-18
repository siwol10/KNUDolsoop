package com.example.SpringBoot.report.service;

import com.example.SpringBoot.report.dto.ReportDTO;
import com.example.SpringBoot.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    public void saveReport(ReportDTO reportDTO) {
        reportRepository.saveReport(reportDTO);
    }

    public ReportDTO findFirstReport(Long resNum) {
        return reportRepository.findFirstReport(resNum);
    }

    public ReportDTO findByReportNum(Long reportNum) {
        return reportRepository.findByReportNum(reportNum);
    }

    public Long findPreNum(Long resNum, LocalDate careDate) {
        return reportRepository.findPreNum(resNum, careDate);
    }

    public Long findNextNum(Long resNum, LocalDate careDate) {
        return reportRepository.findNextNum(resNum, careDate);
    }
}
