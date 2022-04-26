package com.satan.service.impl;

import com.satan.entity.CopyDataToMultiBucketDo;
import com.satan.entity.CreateDirDo;
import com.satan.entity.DelHdfsDirDo;
import com.satan.service.HdfsService;
import com.satan.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

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
  public String delHdfsDir(DelHdfsDirDo delHdfsDirDo) throws Exception {
    FileSystem fs = null;
    String mes = "";
    try {
      fs = getFileSystem(Constants.HDFS_USER);
      // 删除对应路径数据 例如：/hdfsPath/flinkVersion/bucketID/
      for (String bucketId : delHdfsDirDo.getBucketIds()) {
        String bucketPath = flinkJarBasePath + "/" + delHdfsDirDo.getTag() + "/" + bucketId;
        if (fs.exists(new Path(bucketPath))) { // if the file exists
          fs.delete(new Path(bucketPath), true);
        } else { // if the file not exists
          mes = "bucketID is not exist:" + bucketPath;
          log.info(mes);
        }
      }
      mes = String.format("all buckets have been deleted!");
      return mes;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }


  @Override
  public String copyHDFSToMultiBucket(CopyDataToMultiBucketDo copyDataToMultiBucketDo) throws Exception {
    FileSystem fs = null;
    String message = null;
    try {
      fs = getFileSystem(Constants.HDFS_USER);
      String hdfsSourceBucket = flinkJarBasePath + "/" + copyDataToMultiBucketDo.getTag() + "/" + copyDataToMultiBucketDo.getSourceBucketID();
      for (String targetBucketID : copyDataToMultiBucketDo.getTargetBucketIDs()) {
        String hdfsTargetBucket = flinkJarBasePath + "/" + copyDataToMultiBucketDo.getTag() + "/" + targetBucketID;
        org.apache.hadoop.fs.FileUtil.copy(fs, new Path(hdfsSourceBucket), fs, new Path(hdfsTargetBucket), false, fs.getConf());

      }
      message = "copy data to all bucket success";
      return message;
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      throw e;

    }
  }
}
