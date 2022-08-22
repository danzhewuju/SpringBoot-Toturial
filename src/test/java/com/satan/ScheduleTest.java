package com.satan;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Timer;
import java.util.TimerTask;

@Slf4j
//@SpringBootTest()
@RunWith(SpringRunner.class)
public class ScheduleTest {

    @Test
    public void TimeTest() {

        Timer timer = new Timer("test-1");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.info("name:{}", Thread.currentThread().getName());
            }
        }, 1000);

    }
}
