package com.example.employee_management.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SystemScheduleTask {

    private static final Logger log = LoggerFactory.getLogger(SystemScheduleTask.class);

    // fixedRate = 30000ms
    @Scheduled(fixedRate = 30000)
    public void reportSystemStatus() {
        log.info("[SCHEDULED TASK] System running");
    }
}