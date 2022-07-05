package com.satan.service.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DeleteDirSchedule {
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteDir() {
        System.out.println("deleteDir");
    }
}
