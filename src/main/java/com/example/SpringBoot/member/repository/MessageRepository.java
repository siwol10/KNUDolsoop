package com.example.SpringBoot.member.repository;

import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class MessageRepository {

    private final SqlSessionTemplate sql;

    public void save(Long memNum, boolean agreement) {
        Map<String, Object> param = new HashMap<>();
        param.put("memNum", memNum);
        param.put("agreement", agreement);
        sql.insert("Message.save", param);
    }
}