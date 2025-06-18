package com.example.SpringBoot;

import com.example.SpringBoot.reservation.service.ReservationService;
import com.example.SpringBoot.urgent.service.UrgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Scheduler {
    private final UrgentService urgentService;
    private final ReservationService reservationService;

    @Scheduled(cron = "0 0 0 * * *")
    public void schedule() {
        urgentService.findExpiredForms();
        reservationService.updateStateByResDate();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void appStartSchedule() {
        urgentService.findExpiredForms();
        reservationService.updateStateByResDate();
    }
}
