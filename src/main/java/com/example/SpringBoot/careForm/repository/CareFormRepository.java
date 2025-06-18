package com.example.SpringBoot.careForm.repository;

import com.example.SpringBoot.careForm.dto.CareFormDTO;
import com.example.SpringBoot.careForm.dto.ReviewForCareFormDTO;
import com.example.SpringBoot.reservation.dto.ReservationDTO;
import com.example.SpringBoot.reservation.dto.ReservationDateDTO;
import com.example.SpringBoot.review.dto.ReviewAverageNoArgsDTO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CareFormRepository {
    private final SqlSessionTemplate sql;

    public void save(CareFormDTO careFormDTO) {
        sql.insert("CareForm.save", careFormDTO);
    }

    public CareFormDTO findByMemNum(Long memNum) {
        return sql.selectOne("CareForm.findByMemNum", memNum);
    }


    public List<CareFormDTO> findByFilters(Map map) {
        return sql.selectList("CareForm.findByFilters",map);
    }

    public void saveReservation(ReservationDTO reservation) {
        sql.insert("CareForm.saveReservation", reservation);
    }

    public void saveReservationDate(ReservationDateDTO dateDTO) {
        sql.insert("CareForm.saveReservationDate", dateDTO);
    }

    public List<ReviewForCareFormDTO> findReviewByTargetNum(String targetNum){
        return sql.selectList("CareForm.findReviewByTargetNum", targetNum);
    }

    public void updateCareForm(CareFormDTO careForm) {
        sql.update("CareForm.updateCareForm", careForm);
    }

    public void deleteCareForm(Long memNum) {
        sql.delete("CareForm.deleteCareForm", memNum);
    }

    public List<ReviewAverageNoArgsDTO> findAverageStarsForAllSitters() {
        return sql.selectList("Resume.findAverageStarsForAllSitters");
    }
}
