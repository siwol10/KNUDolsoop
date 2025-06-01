package com.example.SpringBoot.urgent.repository;

import com.example.SpringBoot.resume.dto.ResumeDTO;
import com.example.SpringBoot.urgent.dto.UrgentFormDTO;
import com.example.SpringBoot.urgent.dto.UrgentReservationDTO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UrgentRepository {
    private final SqlSessionTemplate sql;

    public void saveForm(UrgentFormDTO urgentFormDTO) {
        sql.insert("urgent.save", urgentFormDTO);
    }

    public List<String> findByRegion(String region) {
        return sql.selectList("urgent.findByRegion", region);
    }

    public List<UrgentFormDTO> findMyForms(Long memNum, int offset, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("memNum", memNum);
        params.put("offset", offset);
        params.put("pageSize", pageSize);

        return sql.selectList("urgent.findMyForms", params);
    }

    public int countMyForms(Long memNum) {
        return sql.selectOne("urgent.countMyForms", memNum);
    }

    public ResumeDTO findResume(Long formNum) {
        return sql.selectOne("urgent.findResume", formNum);
    }

    public UrgentFormDTO formView(Long formNum) {
        return sql.selectOne("urgent.findByFormNum", formNum);
    }

    public void deleteForm (Long formNum) {
        sql.delete("urgent.delete", formNum);
    }

    public List<UrgentReservationDTO> findMyReservations(Long memNum, int offset, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("memNum", memNum);
        params.put("offset", offset);
        params.put("pageSize", pageSize);

        return sql.selectList("urgent.findMyReservations", params);
    }

    public int countMyReservations(Long memNum) {
        return sql.selectOne("urgent.countMyReservations", memNum);
    }

    public void reservation(UrgentReservationDTO urgentReservationDTO, Long formNum) {
        sql.insert("urgent.reservation", urgentReservationDTO);
        sql.update("urgent.updateState", formNum);
    }

    public List<UrgentFormDTO> findAllForms(int offset, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("pageSize", pageSize);

        return sql.selectList("urgent.findAll", params);
    }

    public int countAllForms() {
        return sql.selectOne("urgent.countAll");
    }

    public List<UrgentFormDTO> findFormsByRegion(String region, int offset, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("region", region);
        params.put("offset", offset);
        params.put("pageSize", pageSize);

        return sql.selectList("urgent.findFormsByRegion", params);
    }

    public int countFormsByRegion() {
        return sql.selectOne("urgent.countFormsByRegion");
    }

    public List<Long> findExpiredForms() {
        return sql.selectList("urgent.findExpiredForms");
    }

    public void updateExpiredForms(List<Long> formList) {
        if(formList != null && !formList.isEmpty()) {
            sql.update("urgent.updateExpiredForms", formList);
        }
    }
}
