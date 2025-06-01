package com.example.SpringBoot.urgent.service;

import com.example.SpringBoot.resume.dto.ResumeDTO;
import com.example.SpringBoot.urgent.MessageEvent;
import com.example.SpringBoot.urgent.dto.UrgentFormDTO;
import com.example.SpringBoot.urgent.dto.UrgentReservationDTO;
import com.example.SpringBoot.urgent.repository.UrgentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UrgentService {
    private final UrgentRepository urgentRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void saveForm(UrgentFormDTO urgentFormDTO, String region) {
        urgentRepository.saveForm(urgentFormDTO);
        eventPublisher.publishEvent(new MessageEvent(region));
    }

    public List<UrgentFormDTO> findMyForms(Long memNum, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return urgentRepository.findMyForms(memNum, offset,pageSize);
    }

    public int countMyForms(Long memNum) {
        return urgentRepository.countMyForms(memNum);
    }

    public ResumeDTO findResume(Long formNum) {
        return urgentRepository.findResume(formNum);
    }

    public UrgentFormDTO formView(Long formNum) {
        return urgentRepository.formView(formNum);
    }

    public void deleteForm(Long formNum) {
        urgentRepository.deleteForm(formNum);
    }

    public List<UrgentReservationDTO> findMyReservations(Long memNum, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return urgentRepository.findMyReservations(memNum, offset,pageSize);
    }

    public int countMyReservations(Long memNum) {
        return urgentRepository.countMyReservations(memNum);
    }

    @Transactional
    public void reservation(UrgentReservationDTO urgentReservationDTO, Long formNum) {
        urgentRepository.reservation(urgentReservationDTO, formNum);
    }

    public List<UrgentFormDTO> findAllForms(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return urgentRepository.findAllForms(offset, pageSize);
    }

    public int countAllForms() {
        return urgentRepository.countAllForms();
    }

    public List<UrgentFormDTO> findFormsByRegion(String region, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return urgentRepository.findFormsByRegion(region, offset, pageSize);
    }

    public int countFormsByRegion() {
        return urgentRepository.countFormsByRegion();
    }

    @Transactional
    public void findExpiredForms() {
        List<Long> formList = urgentRepository.findExpiredForms();
        urgentRepository.updateExpiredForms(formList);
    }
}
