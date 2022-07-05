package com.satan.service.impl;

import com.amazonaws.util.IOUtils;
import com.satan.entity.*;
import com.satan.service.HdfsService;
import com.satan.service.MultiThreadsService;
import com.satan.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
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

  @Autowired MultiThreadsService multiThreadsService;


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
  public String createMultiBucketDirectories(CreateBucketDirectoriesDo createBucketDirectoriesDo) throws Exception {
    FileSystem fs = null;
    String mes;
    try {
      fs = getFileSystem(Constants.HDFS_USER);
      // 所需要创建的路径 例如：/hdfsPath/flinkVersion/bucketID/
      for (String bucketId : createBucketDirectoriesDo.getBucketIDs()) {
        String bucketPath = flinkCIPath + "/" + createBucketDirectoriesDo.getFlinkVersion() + "/" + bucketId;
        if (fs.exists(new Path(bucketPath))) {
          mes = "bucketID already exists:" + bucketPath;
        } else {
          fs.mkdirs(new Path(bucketPath));
        }
        FsPermission permission = new FsPermission(FsAction.ALL, FsAction.READ_WRITE, FsAction.READ_WRITE);
        fs.setPermission(new Path(bucketPath), permission);
      }

      mes = "create hdfs dir success, path is " + createBucketDirectoriesDo.getBucketIDs().toString();
      log.info("create hdfs dir success, path is {}", createBucketDirectoriesDo.getBucketIDs());
      return mes;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }finally {
      IOUtils.closeQuietly(fs, null);
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
      String delDir = flinkCIPath + "/" + delBucketsDataDo.getFlinkVersion();
      if(!fs.exists(new Path(delDir))) {
        return null;
      }
      if (delBucketsDataDo.getBucketIDs() == null || delBucketsDataDo.getBucketIDs().size() == 0) {
        FileStatus[] fileStatuses = fs.listStatus(new Path(delDir));
        List<String> names = new ArrayList<>();
        Arrays.stream(fileStatuses).forEach(one -> names.add(one.getPath().getName()));
        delBucketsDataDo.setBucketIDs(names.stream().filter(item -> item.indexOf(Constants.BUCKET) == 0).collect(Collectors.toList()));
      }
      for (String bucketId : delBucketsDataDo.getBucketIDs()) {
        String bucketPath = flinkCIPath + "/" + delBucketsDataDo.getFlinkVersion() + "/" + bucketId;
        if (fs.exists(new Path(bucketPath))) { // if the file exists
          fs.delete(new Path(bucketPath), true);
        } else { // if the file not exists
          mes = "bucketID is not exist:" + bucketPath;
          log.info(mes);
        }
      }
      // if directory is null, which will be deleted.
      String bucketDir = flinkCIPath + "/" + delBucketsDataDo.getFlinkVersion();
      FileStatus[] fileStatuses = fs.listStatus(new Path(bucketDir));
      if (fileStatuses.length == 0) {
        fs.delete(new Path(bucketDir), false);
      }
      mes = "all buckets " + delBucketsDataDo.getBucketIDs() + " have been deleted! ";
      return mes;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }finally {
      IOUtils.closeQuietly(fs, null);
    }
  }

  @Override
  @Async("taskExecutor")
  public void uploadFlinkToMultiBuckets(DeployGrayReleaseDo deployGrayReleaseDo) throws Exception {
    FileSystem fs = null;
    CopyOnWriteArrayList<String> sourcePaths = new CopyOnWriteArrayList<>();
    Queue<String> targetQueue = new ConcurrentLinkedQueue<>();
    try {
      fs = getFileSystem(Constants.HDFS_USER);
      FsPermission permission =
              new FsPermission(FsAction.ALL, FsAction.READ_WRITE, FsAction.READ_WRITE);
      String sourcePath = flinkCIPath + "/" + deployGrayReleaseDo.getSourceFlinkVersion();
      String targetDir =
              flinkCIPath + "/" + deployGrayReleaseDo.getTargetFlinkVersion();
      if (!fs.exists(new Path(targetDir))) {
        fs.mkdirs(new Path(targetDir));
      }
      deployGrayReleaseDo.getTargetBucketIDs().forEach(one -> targetQueue.add(targetDir + "/" + one));
      String newSourcePath = targetQueue.poll();
      assert newSourcePath != null;
      fs.rename(new Path(sourcePath), new Path(newSourcePath));
      sourcePaths.add(newSourcePath);
      while (!targetQueue.isEmpty()) {
        List<CompletableFuture<String>> futures = new CopyOnWriteArrayList<>();
        for (String path : sourcePaths) {
          if (!targetQueue.isEmpty()) {
            futures.add(multiThreadsService.multiThreadsCopy(path, targetQueue.poll(), fs, permission));
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
      log.info("upload flink to multi buckets success! {}", deployGrayReleaseDo.getTargetBucketIDs());
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      throw e;
    }finally {
      IOUtils.closeQuietly(fs, null);
    }
  }

  @Override
  public void copySingleBucketDataToBase(RandomCopySingleBucketDataDo randomCopySingleBucketDataDo) throws Exception {
    FileSystem fs = null;
    String mes = null;
    try {
      fs = getFileSystem(Constants.HDFS_USER);
      String hdfsDir = flinkCIPath + "/" + randomCopySingleBucketDataDo.getSourceFlinkVersion();
      FileStatus[] fileStatuses = fs.listStatus(new Path(hdfsDir));
      if (fileStatuses == null || fileStatuses.length == 0) {
        throw new NullPointerException();
      }
      ArrayList<String> buckets = new ArrayList<>();
      Arrays.stream(fileStatuses).forEach(one -> {
        buckets.add(one.getPath().getName());
      });
      String bucketId = buckets.get(0);
      String hdfsSourceBucket = flinkCIPath + "/" + randomCopySingleBucketDataDo.getSourceFlinkVersion() + "/" + bucketId;
      String hdfsTargetPath = flinkCIPath;
      org.apache.hadoop.fs.FileUtil.copy(fs, new Path(hdfsSourceBucket), fs, new Path(hdfsTargetPath), false, true, fs.getConf());
      hdfsSourceBucket = flinkCIPath + "/" + bucketId;
      hdfsTargetPath = flinkCIPath + "/" + randomCopySingleBucketDataDo.getTargetFlinkVersion();
      fs.rename(new Path(hdfsSourceBucket), new Path(hdfsTargetPath));
      FsPermission permission = new FsPermission(FsAction.ALL, FsAction.READ_WRITE, FsAction.READ_WRITE);
      fs.setPermission(new Path(hdfsTargetPath), permission);
      mes = "copy data to " + hdfsTargetPath + " bucket success";
      log.info(mes);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }finally {
      IOUtils.closeQuietly(fs, null);
    }
  }

  @Override
  public void deployHotfixRelease(DeployHotFixReleaseDo deployHotFixReleaseDo) throws Exception {
    FileSystem fs = null;
    String mes = null;
    try {
      fs = getFileSystem(Constants.HDFS_USER);
      FsPermission permission = new FsPermission(FsAction.ALL, FsAction.READ_WRITE, FsAction.READ_WRITE);
      DelBucketsDataDo delBucketsDataDo = new DelBucketsDataDo();
      delBucketsDataDo.setFlinkVersion(deployHotFixReleaseDo.getFlinkVersion());
      deleteMultiBucketDirectories(delBucketsDataDo);
      String sourcePath = flinkCIPath + "/" + deployHotFixReleaseDo.getSourceFlinkVersion();
      String targetPath = flinkCIPath + "/" + deployHotFixReleaseDo.getTargetFlinkVersion();
      fs.rename(new Path(sourcePath), new Path(targetPath));
      fs.setPermission(new Path(targetPath), permission);
      mes = "deploy hotfix release success!";
      log.info(mes);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }finally {
      IOUtils.closeQuietly(fs, null);
    }
  }

  @Override
  public String pollLastGrayDeployVersion(String flinkVersion) throws IOException {
    FileSystem fs = null;
    try {
      fs = getFileSystem(Constants.HDFS_USER);
      String grayVersionTemporaryFile = flinkCIPath + "/" + flinkVersion + "/" + Constants.LAST_GRAY_VERSION;
      String lastGrayDeployVersion;
      if (!fs.exists(new Path(grayVersionTemporaryFile))) {
        return null;
      } else {
        FSDataInputStream in = fs.open(new Path(grayVersionTemporaryFile));
        lastGrayDeployVersion = org.apache.commons.io.IOUtils.toString(in, StandardCharsets.UTF_8);
        in.close();
        fs.delete(new Path(grayVersionTemporaryFile), false);
      }
      return lastGrayDeployVersion;

    } catch (Exception e) {
      e.printStackTrace();
      throw new IOException();
    }finally {
      IOUtils.closeQuietly(fs, null);
    }
  }

  @Override
  public boolean setLastGrayDeployVersion(String flinkVersion, String grayDeployVersion) throws IOException {
    FileSystem fs = null;
    try {
      fs = getFileSystem(Constants.HDFS_USER);
      FsPermission permission =
              new FsPermission(FsAction.ALL, FsAction.READ_WRITE, FsAction.READ_WRITE);
      String grayVersionTemporaryFile = flinkCIPath + "/" + flinkVersion + "/" + Constants.LAST_GRAY_VERSION;
      FSDataOutputStream out = fs.create(new Path(grayVersionTemporaryFile));
      out.writeBytes(grayDeployVersion);
      out.close();
      fs.setPermission(new Path(grayVersionTemporaryFile), permission);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      throw new IOException();
    }finally {
      IOUtils.closeQuietly(fs, null);
    }
  }

  @Override
  public List<String> getRandomBucketsList(String flinkVersion) throws Exception {
    FileSystem fs = null;
    try {
      fs = getFileSystem(Constants.HDFS_USER);
      String hdfsDir = flinkCIPath + "/" + flinkVersion;
      try{
        FileStatus[] fileStatuses = fs.listStatus(new Path(hdfsDir));
        List<String> bucketIds = new ArrayList<>();
        List<String> finalNames = new ArrayList<>();
        Arrays.stream(fileStatuses).forEach(one -> finalNames.add(one.getPath().getName()));
        finalNames.stream().filter(one -> one.indexOf(Constants.BUCKET) == 0 && one.contains(Constants.BUCKEY_SPLIT)).forEach(one->bucketIds.add(one.split("-")[1]));
        return bucketIds;
      }catch (FileNotFoundException e){
        return new ArrayList<>();
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception(e.getMessage());
    }finally {
      IOUtils.closeQuietly(fs, null);
    }

  }

  @Override
  public String renameFlinkTagName(RenameFlinkTagDo renameFlinkTagDo) throws Exception {
    FileSystem fs = null;
    try {
      fs = getFileSystem(Constants.HDFS_USER);
      FsPermission permission = new FsPermission(FsAction.ALL, FsAction.READ_WRITE, FsAction.READ_WRITE);
      String originalTagPath = flinkCIPath + "/" + renameFlinkTagDo.getOriginalTagName();
      String newTagNamePath = flinkCIPath + "/" + renameFlinkTagDo.getNewTagName();
      if (!fs.exists(new Path(originalTagPath))) {
        throw new Exception("original tag path not exist!");
      } else {
        fs.rename(new Path(originalTagPath), new Path(newTagNamePath));
        fs.setPermission(new Path(newTagNamePath), permission);
      }
      return String.format("%s is renamed to %s", originalTagPath, newTagNamePath);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }finally {
      IOUtils.closeQuietly(fs, null);
    }
  }
}
