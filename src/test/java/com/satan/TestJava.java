package com.satan;

import org.apache.flink.api.common.time.Time;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TestJava {

    public static final long  RESET_TASK_FAILURE_COUNT_TIME = 60*1000L;

    public static void main(String[] args) throws IOException {

       if(new File("src/main/resources/"+"yarn/_metadata").exists()){
           System.out.println("文件存在！！！");
       }



    }
}
