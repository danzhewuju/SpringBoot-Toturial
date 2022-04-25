package com.satan.controller;

import com.satan.common.Result;
import com.satan.entity.CopyDataToMultiBucketDo;
import com.satan.entity.CreateDirDo;
import com.satan.entity.DelHdfsDirDo;
import com.satan.service.HdfsService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequestMapping("/api/hdfs")
@RestController
@Slf4j
public class HdfsHandle {

  @Autowired HdfsService hdfsService;

  @ApiOperation(value = "根据bucket id创建hdfs文件夹", httpMethod = "POST")
  @PostMapping(value = "/ci/flink/create/hdfs/dir", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<String> createHdfsDir(
      HttpServletRequest request,
      @RequestBody @Valid CreateDirDo createDirDo,
      @RequestHeader("Alter-Token") String token) {

    String res = null;
    try {
      res = hdfsService.createHdfsDir(createDirDo);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Result.fail(-1002, e.getMessage(), e);
    }
    return Result.succ(res);
  }

  @ApiOperation(value = "根据bucket id删除hdfs文件夹", httpMethod = "POST")
  @PostMapping(value = "/ci/flink/delete/hdfs/dir", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<String> deleteHdfsDir(
      HttpServletRequest request,
      @RequestBody @Valid DelHdfsDirDo delHdfsDirDo,
      @RequestHeader("Alter-Token") String token) {
    String res = null;
    try {
      res = hdfsService.delHdfsDir(delHdfsDirDo);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Result.fail(-1002, e.getMessage(), e);
    }
    return Result.succ(res);
  }

  @ApiOperation(value = "bucket id 之间复制数据 中", httpMethod = "POST")
  @PostMapping(value = "/ci/flink/copy/bucket", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<String> copyBucketToMultiBuckets(
      HttpServletRequest request,
      @Valid @RequestBody CopyDataToMultiBucketDo copyDataToMultiBucketDo,
      @RequestHeader("Alter-Token") String token) {
    String res = null;
    try {
      res = hdfsService.copyHDFSToMultiBucket(copyDataToMultiBucketDo);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Result.fail(-1002, e.getMessage(), e);
    }
    return Result.succ(res);
  }
}
