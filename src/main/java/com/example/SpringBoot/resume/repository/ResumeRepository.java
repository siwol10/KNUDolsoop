package com.example.SpringBoot.resume.repository;

import com.example.SpringBoot.resume.dto.ResumeDTO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

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
}
