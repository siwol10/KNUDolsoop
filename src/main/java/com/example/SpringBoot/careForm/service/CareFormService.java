package com.example.SpringBoot.careForm.service;

import com.example.SpringBoot.careForm.dto.CareFormDTO;
import com.example.SpringBoot.careForm.dto.ReviewForCareFormDTO;
import com.example.SpringBoot.careForm.repository.CareFormRepository;
import com.example.SpringBoot.reservation.dto.ReservationDTO;
import com.example.SpringBoot.reservation.dto.ReservationDateDTO;
import com.example.SpringBoot.review.dto.ReviewAverageNoArgsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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

    public List<CareFormDTO> findByFilters(Map map) {
        return careFormRepository.findByFilters(map);
    }

    public CareFormDTO getByMemNum(Long memNum) {
        return careFormRepository.findByMemNum(memNum);
    }

    @Transactional
    public void saveReservation(ReservationDTO reservation) {
        careFormRepository.saveReservation(reservation);
        Long resNum = reservation.getResNum();
        for (ReservationDateDTO dateDTO : reservation.getDate()) {
            dateDTO.setResNum(resNum);
            careFormRepository.saveReservationDate(dateDTO);
        }
    }
    public List<ReviewForCareFormDTO> findReviewByTargetNum(String targetNum){
        return careFormRepository.findReviewByTargetNum(targetNum);
    }

    public void updateCareForm(CareFormDTO careFormDTO) {
        careFormRepository.updateCareForm(careFormDTO);
    }

    public void deleteCareForm(Long memNum) {
        careFormRepository.deleteCareForm(memNum);
    }

    public List<ReviewAverageNoArgsDTO> findAverageStarsForAllSitters() {
        return careFormRepository.findAverageStarsForAllSitters();
    }
}
