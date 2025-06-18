package com.example.SpringBoot.reservation.repository;

import com.example.SpringBoot.reservation.dto.ReservationDTO;
import com.example.SpringBoot.reservation.dto.ReservationDateDTO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ReservationRepository {
    private final SqlSessionTemplate sql;

    public List<ReservationDTO> findByTargetNum(Long targetNum, int offset, int page, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("targetNum", targetNum);
        params.put("offset", offset);
        params.put("page", page);
        params.put("pageSize", pageSize);

        return sql.selectList("reservation.findByTargetNum", params);
    }

    public int countByTargetNum(Long targetNum) {
        return sql.selectOne("reservation.countByTargetNum", targetNum);
    }

    public List<ReservationDateDTO> findDate(List<Long> resNumList){
        return sql.selectList("reservation.findDate", resNumList);
    }

    public void updateState(Long resNum, String state) {
        Map<String, Object> params = new HashMap<>();
        params.put("resNum", resNum);
        params.put("state", state);

        sql.update("reservation.updateState", params);
    }

    public List<ReservationDTO> findByMemNum(Long memNum, int offset, int page, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("memNum", memNum);
        params.put("offset", offset);
        params.put("page", page);
        params.put("pageSize", pageSize);

        return sql.selectList("reservation.findByMemNum", params);
    }

    public int countByMemNum(Long memNum) {
        return sql.selectOne("reservation.countByMemNum", memNum);
    }

    public List<ReservationDTO> findAll() {
        return sql.selectList("reservation.findAll");
    }

    public List<ReservationDTO> findOngoing(Long num, int offset, int page, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("num", num);
        params.put("offset", offset);
        params.put("page", page);
        params.put("pageSize", pageSize);

        return sql.selectList("reservation.findOngoing", params);
    }

    public int countOngoing(Long num) {
        return sql.selectOne("reservation.countOngoing", num);
    }

    public List<ReservationDTO> findCompleted(Long num, int offset, int page, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("num", num);
        params.put("offset", offset);
        params.put("page", page);
        params.put("pageSize", pageSize);

        return sql.selectList("reservation.findCompleted", params);
    }

    public int countCompleted(Long num) {
        return sql.selectOne("reservation.countCompleted", num);
    }

    public List<ReservationDTO> findDropped(Long num, int offset, int page, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("num", num);
        params.put("offset", offset);
        params.put("page", page);
        params.put("pageSize", pageSize);

        return sql.selectList("reservation.findDropped", params);
    }

    public int countDropped(Long num) {
        return sql.selectOne("reservation.countDropped", num);
    }

    public void updateMemReview(Long resNum) {
        sql.update("reservation.updateMemReview", resNum);
    }

    public void updateTargetReview(Long resNum) {
        sql.update("reservation.updateTargetReview", resNum);
    }

    public ReservationDTO findByResNum(Long resNum) {
        return sql.selectOne("reservation.findByResNum", resNum);
    }
}
