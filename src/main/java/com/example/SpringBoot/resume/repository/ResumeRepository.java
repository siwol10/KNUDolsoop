package com.example.SpringBoot.resume.repository;

import com.example.SpringBoot.reservation.dto.ReservationDTO;
import com.example.SpringBoot.reservation.dto.ReservationDateDTO;
import com.example.SpringBoot.resume.dto.ResumeDTO;
import com.example.SpringBoot.resume.dto.ReviewForResumeDTO;
import com.example.SpringBoot.review.dto.ReviewAverageNoArgsDTO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ResumeRepository {
    private final SqlSessionTemplate sql;

    public void save(ResumeDTO resumeDTO) {
        sql.insert("Resume.save", resumeDTO);
    }
    public ResumeDTO findByMemNum(Long memNum) {
        return sql.selectOne("Resume.findByMemNum", memNum);
    }

    public List<ResumeDTO> findByFilters(Map map) {
        return sql.selectList("Resume.findByFilters",map);
    }

    public void saveReservation(ReservationDTO reservation) {
        sql.insert("Resume.saveReservation", reservation);
    }

    public void saveReservationDate(ReservationDateDTO dateDTO) {
        sql.insert("Resume.saveReservationDate", dateDTO);
    }

    public List<ReviewForResumeDTO> findReviewByTargetNum(String targetNum){
        return sql.selectList("Resume.findReviewByTargetNum", targetNum);
    }

    public void updateResume(ResumeDTO resumeDTO) {
        sql.update("Resume.updateResume", resumeDTO);
    }

    public void deleteResume(Long memNum) {
        sql.delete("Resume.deleteResume", memNum);
    }

    public List<ReviewAverageNoArgsDTO> findAverageStarsForAllSitters() {
        return sql.selectList("Resume.findAverageStarsForAllSitters");
    }

}
