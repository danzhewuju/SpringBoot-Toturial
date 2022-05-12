package com.satan;

import com.satan.entity.UploadDataToMultiBucketsDo;
import com.satan.service.BankService;
import com.satan.service.HdfsService;
import com.satan.service.ServiceMultiThread;
import com.satan.utils.MyThread;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest()
@EnableAsync(proxyTargetClass = true)
public class TestMultiThread {
  @Autowired ServiceMultiThread serviceMultiThread;
  @Autowired HdfsService hdfsService;
  @Autowired BankService bankService;
  private int res = 10000;

  @Test
  public void testThread() throws InterruptedException {
    List<Integer> sources = new CopyOnWriteArrayList<>();
    Queue<Integer> targets = new ConcurrentLinkedQueue<>();
    for (int i = 0; i < 100; i++) {
      targets.add(i + 1);
    }
    sources.add(1);
    while (!targets.isEmpty()) {
      List<CompletableFuture<Integer>> futures = new CopyOnWriteArrayList<>();
      for (int s : sources) {
        if (!targets.isEmpty()) {
          CompletableFuture<Integer> integerCompletableFuture =
              serviceMultiThread.addNumber(s, targets.poll());
          futures.add(integerCompletableFuture);
        } else {
          break;
        }
      }
      CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
      futures.forEach(
          one -> {
            try {
              sources.add(one.get());
            } catch (InterruptedException | ExecutionException e) {
              e.printStackTrace();
            }
          });
    }
    log.info("{}", sources);
  }

  @Test
  public void testHdfsService() throws Exception {
    UploadDataToMultiBucketsDo uploadDataToMultiBucketsDo = new UploadDataToMultiBucketsDo();
    uploadDataToMultiBucketsDo.setSourceFlinkVersion("FLINK-1.11.3.35-Tue");
    uploadDataToMultiBucketsDo.setTargetFlinkVersion("1.11.3");
    ArrayList<String> targetBucketIDs = new ArrayList<>();
    String[] tmp = {
      "bucket-1",
      "bucket-2",
      "bucket-3",
      "bucket-4",
      "bucket-5",
      "bucket-6",
      "bucket-7",
      "bucket-8",
      "bucket-9",
      "bucket-10",
      "bucket-11",
      "bucket-12",
      "bucket-13",
      "bucket-14"
    };
    Arrays.stream(tmp).sequential().forEach(targetBucketIDs::add);
    uploadDataToMultiBucketsDo.setTargetBucketIDs(targetBucketIDs);
    hdfsService.uploadFlinkToMultiBuckets(uploadDataToMultiBucketsDo);
  }

  @Test
  public void testMultiThreads() throws InterruptedException, ExecutionException {

    //    for (int i = 0; i < 1500; i++) {
    //      CompletableFuture<Integer> resource = serviceMultiThread.getResource(res, 5);
    //      res = resource.get();
    //    }
    //    log.info("res is {}", res);
    for (int i = 0; i < 10; i++) {
      bankService.addResource(1);
    }
    Thread.sleep(20000);
    log.info(String.valueOf(BankService.res));
  }

  @Test
  public void testMyThread() {
    for (int i = 0; i < 10; i++) {
      new Thread(new MyThread()).start();
    }
    log.info("my name is "+Thread.currentThread().getName());
  }
}
