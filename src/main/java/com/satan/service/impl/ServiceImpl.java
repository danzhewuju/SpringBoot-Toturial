package com.satan.service.impl;

import com.satan.entity.CreateBucketDirectoriesDo;
import com.satan.entity.DelBucketsDataDo;
import com.satan.entity.RandomCopySingleBucketDataDo;
import com.satan.entity.UploadDataToMultiBucketsDo;
import com.satan.service.HdfsService;
import com.satan.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ServiceImpl implements HdfsService {
  @Value("${hdfs.base.path}")
  private String flinkJarBasePath;

  @Value("${hdfs.base.path}")
  private String flinkCIPath;

  @Value("${hdfs.path}")
  private String hdfsPath;

  public FileSystem getFileSystem(String user)
      throws URISyntaxException, IOException, InterruptedException {
    Configuration configuration = new Configuration();
    configuration.set("dfs.client.use.datanode.hostname", "true");
    FileSystem fs = FileSystem.get(new URI(hdfsPath), configuration, user);

    return fs;
  }

  public List<String> listFile(String path) throws Exception {

    FileSystem fs = null;
    ArrayList<String> list = new ArrayList<>();
    try {
      fs = getFileSystem(Constants.HDFS_USER);
      RemoteIterator<LocatedFileStatus> rfs = fs.listFiles(new Path(path), false);

      if (rfs != null && rfs.hasNext()) {
        LocatedFileStatus fileStatus = rfs.next();
        list.add(fileStatus.getPath().toString());
      }
      FileStatus[] fileStatuses = fs.listStatus(new Path(path));
      if (fileStatuses == null || fileStatuses.length == 0) return list;

      Arrays.stream(fileStatuses)
          .forEach(
              one -> {
                list.add(one.getPath().toString());
              });
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      org.apache.hadoop.io.IOUtils.closeStream(fs);
    }
    return list;
  }

  @Override
  public String createMultiBucketDirectories(CreateBucketDirectoriesDo createBucketDirectoriesDo)
      throws Exception {
    FileSystem fs = null;
    String mes;
    try {
      fs = getFileSystem(Constants.HDFS_USER);
      // 所需要创建的路径 例如：/hdfsPath/flinkVersion/bucketID/
      for (String bucketId : createBucketDirectoriesDo.getBucketIDs()) {
        String bucketPath =
            flinkJarBasePath + "/" + createBucketDirectoriesDo.getFlinkVersion() + "/" + bucketId;
        if (fs.exists(new Path(bucketPath))) {
          mes = "bucketID already exists:" + bucketPath;
        } else {
          fs.mkdirs(new Path(bucketPath));
        }
        FsPermission permission =
            new FsPermission(FsAction.ALL, FsAction.READ_WRITE, FsAction.READ_WRITE);
        fs.setPermission(new Path(bucketPath), permission);
      }

      mes = "create hdfs dir success, path is " + createBucketDirectoriesDo.getBucketIDs();
      log.info("create hdfs dir success, path is {}", createBucketDirectoriesDo.getBucketIDs());
      return mes;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

  @Override
  public String deleteMultiBucketDirectories(DelBucketsDataDo delBucketsDataDo) throws Exception {
    FileSystem fs = null;
    String mes = "";
    try {
      fs = getFileSystem(Constants.HDFS_USER);
      // 删除对应路径数据 例如：/hdfsPath/flinkVersion/bucketID/
      // 没有指定就进行查询并删除全部
      if (delBucketsDataDo.getBucketIDs() == null || delBucketsDataDo.getBucketIDs().size() == 0) {
        String delDir = flinkJarBasePath + "/" + delBucketsDataDo.getFlinkVersion();
        FileStatus[] fileStatuses = fs.listStatus(new Path(delDir));
        List<String> names = new ArrayList<>();
        Arrays.stream(fileStatuses).forEach(one -> names.add(one.getPath().getName()));
        delBucketsDataDo.setBucketIDs(
            names.stream()
                .filter(item -> item.indexOf(Constants.BUCKET) == 0)
                .collect(Collectors.toList()));
      }
      for (String bucketId : delBucketsDataDo.getBucketIDs()) {
        String bucketPath =
            flinkJarBasePath + "/" + delBucketsDataDo.getFlinkVersion() + "/" + bucketId;
        if (fs.exists(new Path(bucketPath))) { // if the file exists
          fs.delete(new Path(bucketPath), true);
        } else { // if the file not exists
          mes = "bucketID is not exist:" + bucketPath;
          log.info(mes);
        }
      }
      mes = "all buckets " + delBucketsDataDo.getBucketIDs() + "have been deleted! ";
      return mes;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

  @Override
  public String copySingleBucketDataToBase(
      RandomCopySingleBucketDataDo randomCopySingleBucketDataDo) throws Exception {
    FileSystem fs = null;
    String message = null;
    try {
      fs = getFileSystem(Constants.HDFS_USER);
      String hdfsDir =
          flinkJarBasePath + "/" + randomCopySingleBucketDataDo.getSourceFlinkVersion();
      FileStatus[] fileStatuses = fs.listStatus(new Path(hdfsDir));
      if (fileStatuses == null || fileStatuses.length == 0) {
        throw new NullPointerException();
      }
      ArrayList<String> buckets = new ArrayList<>();
      Arrays.stream(fileStatuses)
          .forEach(
              one -> {
                buckets.add(one.getPath().getName());
              });
      String bucketId = buckets.get(0);
      String hdfsSourceBucket =
          flinkJarBasePath
              + "/"
              + randomCopySingleBucketDataDo.getSourceFlinkVersion()
              + "/"
              + bucketId;
      String hdfsTargetPath = flinkJarBasePath;
      org.apache.hadoop.fs.FileUtil.copy(
          fs, new Path(hdfsSourceBucket), fs, new Path(hdfsTargetPath), false, true, fs.getConf());
      hdfsSourceBucket = flinkJarBasePath + "/" + bucketId;
      hdfsTargetPath =
          flinkJarBasePath + "/" + randomCopySingleBucketDataDo.getTargetFlinkVersion();
      fs.rename(new Path(hdfsSourceBucket), new Path(hdfsTargetPath));
      FsPermission permission =
          new FsPermission(FsAction.ALL, FsAction.READ_WRITE, FsAction.READ_WRITE);
      fs.setPermission(new Path(hdfsTargetPath), permission);
      message = "copy data to " + hdfsTargetPath + " bucket success";
      return message;
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      throw e;
    }
  }

  @Override
  public String uploadFlinkToMultiBucket(UploadDataToMultiBucketsDo uploadDataToMultiBucketsDo)
      throws Exception {
    FileSystem fs = null;
    CopyOnWriteArrayList<String> sourcePaths = new CopyOnWriteArrayList<>();
    Queue<String> targetQueue = new ConcurrentLinkedQueue<>();
    try {
      fs = getFileSystem(Constants.HDFS_USER);
      FsPermission permission =
          new FsPermission(FsAction.ALL, FsAction.READ_WRITE, FsAction.READ_WRITE);
      String sourcePath = flinkCIPath + "/" + uploadDataToMultiBucketsDo.getSourceFlinkVersion();
      String targetDir =
          flinkJarBasePath + "/" + uploadDataToMultiBucketsDo.getTargetFlinkVersion();
      if (!fs.exists(new Path(targetDir))) {
        fs.mkdirs(new Path(targetDir));
      }
      uploadDataToMultiBucketsDo
          .getTargetBucketIDs()
          .forEach(one -> targetQueue.add(targetDir + "/" + one));
      //      for (String bucketId : uploadDataToMultiBucketsDo.getTargetBucketIDs()) {
      //        String targetPath = targetDir + "/" + bucketId;
      //        org.apache.hadoop.fs.FileUtil.copy(
      //            fs, new Path(sourcePath), fs, new Path(targetPath), false, true, fs.getConf());
      //        fs.setPermission(new Path(targetPath), permission);
      //      }
      sourcePaths.add(sourcePath);
      while (targetQueue.size() > 0) {
        List<CompletableFuture<String>> futures = new CopyOnWriteArrayList<>();
        for (String path : sourcePaths) {
          if (targetQueue.size() > 0) {
            futures.add(multiThreadsCopy(path, targetQueue.poll(), fs, permission));
          } else {
            break;
          }
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        futures.forEach(
            one -> {
              try {
                sourcePaths.add(one.get());
              } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
              } catch (ExecutionException e) {
                e.printStackTrace();
              }
            });
      }
      return "ci-cd data upload to " + uploadDataToMultiBucketsDo.getTargetBucketIDs();
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      throw e;
    }
  }

  @Async("taskExecutor")
  public CompletableFuture<String> multiThreadsCopy(
      String sourcePaths, String targetPaths, FileSystem fs, FsPermission fsPermission)
      throws IOException {
    FileUtil.copy(fs, new Path(sourcePaths), fs, new Path(targetPaths), false, true, fs.getConf());
    fs.setPermission(new Path(targetPaths), fsPermission);
    return CompletableFuture.completedFuture(targetPaths);
  }
}
