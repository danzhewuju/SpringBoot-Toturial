package com.satan;

import org.apache.flink.api.common.time.Time;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TestJava {

    public static final long  RESET_TASK_FAILURE_COUNT_TIME = 60*1000L;

    public static void main(String[] args) throws IOException {

        AtomicInteger currentRestartAttempt = new AtomicInteger(0);
        for (int i = 0; i < 10; i++) {
            currentRestartAttempt.incrementAndGet();
        }
        System.out.println(currentRestartAttempt.get());
        Time time = Time.of(-1, TimeUnit.MILLISECONDS);
        System.out.println(time.toMilliseconds());


    }
}
