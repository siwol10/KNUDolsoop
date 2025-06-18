package com.example.SpringBoot.file.repository;

import com.example.SpringBoot.file.dto.FileObjectDTO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class FileObjectRepository {

    private final SqlSessionTemplate sql;

    public int insert(FileObjectDTO file) {
        return sql.insert("fileObject.insert", file);
    }

    public List<FileObjectDTO> findByReference(Map params) {
        return sql.selectList("fileObject.findByReference", params);
    }

    public FileObjectDTO findById(Long id) {
        return sql.selectOne("fileObject.findById", id);
    }

    public int deleteById(Long id) {
        return sql.delete("fileObject.deleteById", id);
    }
}