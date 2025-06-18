package com.example.SpringBoot.review.service;

import com.example.SpringBoot.reservation.dto.ReservationDTO;
import com.example.SpringBoot.reservation.repository.ReservationRepository;
import com.example.SpringBoot.review.dto.ReviewAverageDTO;
import com.example.SpringBoot.review.dto.ReviewDTO;
import com.example.SpringBoot.review.repository.ReviewAverageRepository;
import com.example.SpringBoot.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewAverageRepository reviewAverageRepository;

    @Transactional
    public void saveReview(ReviewDTO reviewDTO) {
        reviewRepository.saveReview(reviewDTO); // 리뷰 저장

        // ▼ 평점 저장
        ReservationDTO reservationDTO = reservationRepository.findByResNum(reviewDTO.getResNum());

        Long writerNum = reviewDTO.getWriter();
        Long sitterNum = 0L;

        // 돌보미 찾기
        if(reservationDTO.getMemNum() == writerNum) {
            sitterNum = reservationDTO.getTargetNum();
        } else if(reservationDTO.getTargetNum() == writerNum) {
            sitterNum = reservationDTO.getMemNum();
        }

        ReviewAverageDTO reviewAverageDTO = reviewAverageRepository.findByMemNum(sitterNum);

        if(reviewAverageDTO == null) { // 저장된 평점이 없으면 새로 저장
            reviewAverageDTO = new ReviewAverageDTO(sitterNum, reviewDTO.getStar().floatValue(), 1, reviewDTO.getStar().floatValue());
            reviewAverageRepository.saveAverage(reviewAverageDTO);
        } else { // 평점 업데이트
            float total = reviewAverageDTO.getTotal() + reviewDTO.getStar();
            int count = reviewAverageDTO.getCount() + 1;
            float average = total / count;

            reviewAverageRepository.updateAverage(sitterNum, total, count, average);
        }
    }
}
