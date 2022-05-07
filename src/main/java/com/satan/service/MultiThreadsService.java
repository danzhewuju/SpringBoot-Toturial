package com.satan.service;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.permission.FsPermission;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface MultiThreadsService {

    CompletableFuture<String> multiThreadsCopy(String sourcePaths, String targetPaths, FileSystem fs, FsPermission fsPermission) throws IOException;
}
