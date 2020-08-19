package com.muffin.web.util;


import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
public class StockScheduler {

    @Scheduled(cron = "0 0/1 * 1/1 * ?")
    public void testShcedule() {
        System.out.println("~~~~~~~~~~~~~~~~~~scheduled~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

}


// 0 0 0/1 1/1 * ? *