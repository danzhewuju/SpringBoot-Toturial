package com.satan.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@Data
@Slf4j
public class BankService {
  public static int  res = 100000;
  @Autowired ServiceMultiThread serviceMultiThread;

   synchronized public void add(int a){
    res += a;
  }

  @Async("taskExecutor")
  public void addResource(int a) throws InterruptedException, ExecutionException {
    //        CompletableFuture<Integer> resource = serviceMultiThread.getResource(res, a);
    //        res = resource.get();
    log.info(Thread.currentThread().getName());
    for (int i = 0; i < 10000; i++) {
      add(a);
    }
  }

  @Async("taskExecutor")
  public void putResource(int a) throws InterruptedException, ExecutionException {
    //        CompletableFuture<Integer> resource = serviceMultiThread.putResource(res, a);
    //        res = resource.get();
    log.info(Thread.currentThread().getName());

    for (int i = 0; i < 10000; i++) {
      res += a;
    }
  }
}
