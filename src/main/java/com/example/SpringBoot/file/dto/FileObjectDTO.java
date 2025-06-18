package com.example.SpringBoot.file.dto;

import lombok.Data;

@Data
public class FileObjectDTO {
    private Long id;
    private String referenceType;
    private Long referenceId;
    private String fileType;
    private String url;
    private String originalName;
}