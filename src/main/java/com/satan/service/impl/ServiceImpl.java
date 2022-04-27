package com.satan.service.impl;

import com.satan.entity.CopyDataToMultiBucketDo;
import com.satan.entity.CopyDeployDataDo;
import com.satan.entity.CreateDirDo;
import com.satan.entity.DelHdfsDirDo;
import com.satan.service.HdfsService;
import com.satan.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ServiceImpl implements HdfsService {
  @Value("${hdfs.base.path}")
  private String flinkJarBasePath;

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

      Arrays.stream(fileStatuses).forEach(one -> {
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
  public String createHdfsDir(CreateDirDo createDirDo) throws Exception {
    FileSystem fs = null;
    String mes;
    try {
      fs = getFileSystem(Constants.HDFS_USER);
      // 所需要创建的路径 例如：/hdfsPath/flinkVersion/bucketID/
      for (String bucketId : createDirDo.getBucketIDs()) {
        String bucketPath = flinkJarBasePath + "/" + createDirDo.getTag() + "/" + bucketId;
        if (fs.exists(new Path(bucketPath))) {
          mes = "bucketID already exists:" + bucketPath;
        } else {
          fs.mkdirs(new Path(bucketPath));
        }
      }
      mes = String.format("create hdfs dir success");

      log.info("create hdfs dir success, path is {}", createDirDo.getBucketIDs());
      return mes;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

  @Override
  public String delHdfsBucketDir(DelHdfsDirDo delHdfsDirDo) throws Exception {
    FileSystem fs = null;
    String mes = "";
    try {
      fs = getFileSystem(Constants.HDFS_USER);
      // 删除对应路径数据 例如：/hdfsPath/flinkVersion/bucketID/
      // 没有指定就进行查询
      if (delHdfsDirDo.getBucketIds() == null || delHdfsDirDo.getBucketIds().size() == 0) {
        String delDir = flinkJarBasePath + "/" + delHdfsDirDo.getTag();
        FileStatus[] fileStatuses = fs.listStatus(new Path(delDir));
        delHdfsDirDo.setBucketIds(new ArrayList<>());
        Arrays.stream(fileStatuses).forEach(one -> {
          delHdfsDirDo.getBucketIds().add(one.getPath().getName());
        });
      }
      for (String bucketId : delHdfsDirDo.getBucketIds()) {
        String bucketPath = flinkJarBasePath + "/" + delHdfsDirDo.getTag() + "/" + bucketId;
        if (fs.exists(new Path(bucketPath))) { // if the file exists
          fs.delete(new Path(bucketPath), true);
        } else { // if the file not exists
          mes = "bucketID is not exist:" + bucketPath;
          log.info(mes);
        }
      }
      mes = String.format("all buckets {} have been deleted! ", delHdfsDirDo.getBucketIds());
      return mes;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

  @Override
  public String copyDeployData(CopyDeployDataDo copyDeployDataDo)
      throws Exception {
    FileSystem fs = null;
    String message = null;
    try {
      fs = getFileSystem(Constants.HDFS_USER);
      String hdfsDir = flinkJarBasePath + "/" + copyDeployDataDo.getSourceTag();
      FileStatus[] fileStatuses = fs.listStatus(new Path(hdfsDir));
      if (fileStatuses == null || fileStatuses.length == 0) {
        throw new NullPointerException();
      }
      ArrayList<String> buckets = new ArrayList<>();
      Arrays.stream(fileStatuses).forEach(one -> {
        buckets.add(one.getPath().getName());
      });
      String bucketId = buckets.get(0);
      String hdfsSourceBucket = flinkJarBasePath + "/" + copyDeployDataDo.getSourceTag() + "/" + bucketId;
      String hdfsTargetPath = flinkJarBasePath;
      org.apache.hadoop.fs.FileUtil.copy(fs, new Path(hdfsSourceBucket), fs, new Path(hdfsTargetPath), false,true, fs.getConf());
      hdfsSourceBucket = flinkJarBasePath + "/" + bucketId;
      hdfsTargetPath = flinkJarBasePath + "/" + copyDeployDataDo.getTargetTag();
      fs.rename(new Path(hdfsSourceBucket), new Path(hdfsTargetPath));
      FsPermission permission = new FsPermission(FsAction.ALL, FsAction.READ_WRITE, FsAction.READ_WRITE);
      fs.setPermission(new Path(hdfsTargetPath), permission);
      message = "copy data to " + hdfsTargetPath + " bucket success";
      return message;
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      throw e;
    }
  }
}
