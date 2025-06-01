package com.example.SpringBoot.resume.service;

import com.example.SpringBoot.resume.dto.ResumeDTO;
import com.example.SpringBoot.resume.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;

    public void save(ResumeDTO resumeDTO) {
        resumeRepository.save(resumeDTO);
    }

    public ResumeDTO findByMemNum(Long memNum) {
        return resumeRepository.findByMemNum(memNum);
    }
}
