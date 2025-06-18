package com.example.SpringBoot.member.repository;

import com.example.SpringBoot.member.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final SqlSessionTemplate sql;

    public void save(MemberDTO memberDTO) {
        sql.insert("Member.save", memberDTO);
    }

    public MemberDTO findById(String id) {
        return sql.selectOne("Member.findById", id);
    }
    public MemberDTO findByMemNum(String memNum) {
        return sql.selectOne("Member.findByMemNum", memNum);
    }

    public void updateMember(MemberDTO memberDTO) {
        sql.update("Member.updateMember", memberDTO);
    }
}