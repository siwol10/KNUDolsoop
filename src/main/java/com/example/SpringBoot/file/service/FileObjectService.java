package com.example.SpringBoot.file.service;

import com.example.SpringBoot.file.dto.FileObjectDTO;
import com.example.SpringBoot.file.repository.FileObjectRepository;
import com.example.SpringBoot.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileObjectService {
    private final S3Uploader s3Uploader;
    private final FileObjectRepository fileObjectRepository;

    public List<FileObjectDTO> findByReference(String referenceType, Long referenceId, String fileType) {
        Map<String, Object> params = new HashMap<>();
        params.put("referenceType", referenceType);
        params.put("referenceId", referenceId);
        params.put("fileType", fileType);
        return fileObjectRepository.findByReference(params);
    }

    public void deleteFileByReference(String referenceType, Long referenceId, String fileType) {
        Map<String, Object> params = new HashMap<>();
        params.put("referenceType", referenceType);
        params.put("referenceId", referenceId);
        params.put("fileType", fileType);

        List<FileObjectDTO> files = fileObjectRepository.findByReference(params);

        for (FileObjectDTO file : files) {
            deleteFile(file.getId());
        }
    }

    public void deleteFile(Long id) {
        FileObjectDTO file = fileObjectRepository.findById(id);
        if (file != null) {
            try {
                URL url = new URL(file.getUrl());
                String key = url.getPath().substring(1);
                s3Uploader.deleteFile(key); // S3에서 삭제
            } catch (MalformedURLException e) {
                throw new RuntimeException("잘못된 URL 형식입니다: " + file.getUrl(), e);
            }

            fileObjectRepository.deleteById(id); // DB에서 삭제
        }
    }
}