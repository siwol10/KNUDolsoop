package com.example.SpringBoot.careForm.service;

import com.example.SpringBoot.careForm.dto.CareFormDTO;
import com.example.SpringBoot.careForm.repository.CareFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CareFormService {
    private final CareFormRepository careFormRepository;

    public void save(CareFormDTO careFormDTO) {
        careFormRepository.save(careFormDTO);
    }

    public CareFormDTO findByMemNum(Long memNum) {
        return careFormRepository.findByMemNum(memNum);
    }

}
