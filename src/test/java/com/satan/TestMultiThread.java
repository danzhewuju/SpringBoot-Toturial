package com.satan;

import com.satan.service.ServiceMultiThread;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest()
public class TestMultiThread {
  @Autowired ServiceMultiThread serviceMultiThread;

  @Test
  public void testThread() throws InterruptedException {
    List<Integer> sources = new CopyOnWriteArrayList<>();
    Queue<Integer> targets = new ConcurrentLinkedQueue<>();
    for (int i = 0; i < 50; i++) {
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
}
