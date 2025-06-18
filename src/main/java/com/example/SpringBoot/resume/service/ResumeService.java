package com.example.SpringBoot.resume.service;

import com.example.SpringBoot.reservation.dto.ReservationDTO;
import com.example.SpringBoot.reservation.dto.ReservationDateDTO;
import com.example.SpringBoot.resume.dto.ResumeDTO;
import com.example.SpringBoot.resume.dto.ReviewForResumeDTO;
import com.example.SpringBoot.resume.repository.ResumeRepository;
import com.example.SpringBoot.review.dto.ReviewAverageNoArgsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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

    public List<ResumeDTO> findByFilters(Map map) {
        return resumeRepository.findByFilters(map);
    }

    public ResumeDTO getResumeByMemNum(Long memNum) {
        return resumeRepository.findByMemNum(memNum);
    }

    @Transactional
    public void saveReservation(ReservationDTO reservation) {
        resumeRepository.saveReservation(reservation); // resNum 생성됨

        Long resNum = reservation.getResNum();
        for (ReservationDateDTO dateDTO : reservation.getDate()) {
            dateDTO.setResNum(resNum);
            resumeRepository.saveReservationDate(dateDTO);
        }
    }

    public List<ReviewForResumeDTO> findReviewByTargetNum(String targetNum){
        return resumeRepository.findReviewByTargetNum(targetNum);
    }

    public void updateResume(ResumeDTO resumeDTO) {
        resumeRepository.updateResume(resumeDTO);
    }

    public void deleteResume(Long memNum) {
        resumeRepository.deleteResume(memNum);
    }

    public List<ReviewAverageNoArgsDTO> findAverageStarsForAllSitters() {
        return resumeRepository.findAverageStarsForAllSitters();
    }

}
