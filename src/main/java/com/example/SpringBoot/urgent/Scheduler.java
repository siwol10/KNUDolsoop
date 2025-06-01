package com.example.SpringBoot.urgent;

import com.example.SpringBoot.urgent.service.UrgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Scheduler {
    private final UrgentService urgentService;

    @Scheduled(cron = "0 0 0 * * *")
    public void schedule() {
        urgentService.findExpiredForms();
    }
}