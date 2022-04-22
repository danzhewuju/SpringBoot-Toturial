package com.satan.service.impl;

import com.satan.common.Result;
import com.satan.entity.CreateHdfsDirDo;
import com.satan.entity.DelHdfsDirDo;
import com.satan.service.HdfsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Service
@Slf4j
public class ServiceImpl implements HdfsService {
  @Value("${hdfs.base.path}")
  private String basePath;

  @Value("${hdfs.bucket.path}")
  private String bucketPath;

  @Value("${hdfs.path}")
  private String hdfsPath;

  @Override
  public Result<String> hdfsCreateDir(CreateHdfsDirDo createHdfsDirDo) throws URISyntaxException {

    try {
      Configuration configuration = new Configuration();
      FileSystem fs = FileSystem.get(new URI(hdfsPath), configuration);
      String id = String.valueOf(Math.abs(createHdfsDirDo.getJobId().hashCode() % 100));
      String newPath = bucketPath + "/" + id;
      fs.mkdirs(new Path(newPath));
      return Result.succ(newPath);
    } catch (IOException e) {
      log.info(e.toString());
      return Result.fail("data is wrong");
    }
  }

  @Override
  public Result<String> hdfsDeleteDir(DelHdfsDirDo delHdfsDirDo) {
    Configuration configuration = new Configuration();
    try {
      FileSystem fs = FileSystem.get(new URI(hdfsPath), configuration);
      String id = String.valueOf(Math.abs(delHdfsDirDo.getJobId().hashCode() % 100));
      String newPath = bucketPath + "/" + id;
      if (fs.exists(new Path(newPath))) {
        fs.delete(new Path(newPath), true);
        return Result.succ("del dir :" + newPath);
      } else {
        return Result.fail("bucket id path is not exit! ");
      }
    } catch (IOException e) {
      log.info(e.toString());
      return Result.fail("connect HDFS IO error");
    } catch (URISyntaxException e) {
      log.info(e.toString());
      return Result.fail("connect HDFS URI error");
    }
  }
}
