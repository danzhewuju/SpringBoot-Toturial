package com.satan.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyThread implements Runnable{
    @Override
    public void run() {
        log.info("this is "+Thread.currentThread().getName());
    }
}
