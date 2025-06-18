package com.example.SpringBoot.review.service;

import com.example.SpringBoot.reservation.service.ReservationService;
import com.example.SpringBoot.review.dto.ReviewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationReviewService {
    private final ReservationService reservationService;
    private final ReviewService reviewService;

    @Transactional
    public void saveReview(ReviewDTO reviewDTO, String user) {
        reviewService.saveReview(reviewDTO);
        reservationService.updateReview(reviewDTO.getResNum(), user);
    }
}
