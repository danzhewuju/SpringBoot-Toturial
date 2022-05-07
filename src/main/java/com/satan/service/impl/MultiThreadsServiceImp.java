package com.satan.service.impl;

import com.satan.service.MultiThreadsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class MultiThreadsServiceImp implements MultiThreadsService {

  @Async("taskExecutor")
  public CompletableFuture<String> multiThreadsCopy(
      String sourcePaths, String targetPaths, FileSystem fs, FsPermission fsPermission)
      throws IOException {
    log.info(Thread.currentThread().getName());
    FileUtil.copy(fs, new Path(sourcePaths), fs, new Path(targetPaths), false, true, fs.getConf());
    fs.setPermission(new Path(targetPaths), fsPermission);
    return CompletableFuture.completedFuture(targetPaths);
  }
}
