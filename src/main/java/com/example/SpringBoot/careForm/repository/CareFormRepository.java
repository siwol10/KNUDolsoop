package com.example.SpringBoot.careForm.repository;

import com.example.SpringBoot.careForm.dto.CareFormDTO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

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

}
