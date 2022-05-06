package com.satan.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ServiceMultiThread {

  @Async("taskExecutor")
  public CompletableFuture<String> printName() {
    log.info(Thread.currentThread().getName());
    return CompletableFuture.completedFuture(Thread.currentThread().getName());
  }

  @Async("taskExecutor")
  public CompletableFuture<Integer> addNumber(int a, int b) throws InterruptedException {
    log.info(Thread.currentThread().getName());
    log.info("a is {}, b is {} res is {}",a,b, a + b);
    Thread.sleep(2000);
    return CompletableFuture.completedFuture(a+b);
  }
}
