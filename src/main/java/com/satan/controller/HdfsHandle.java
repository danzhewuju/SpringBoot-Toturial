package com.satan.controller;

import com.satan.common.Result;
import com.satan.entity.CreateDirDo;
import com.satan.entity.DelHdfsDirDo;
import com.satan.entity.DeployThuDo;
import com.satan.entity.DeployTueDo;
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
      res = hdfsService.delHdfsBucketDir(delHdfsDirDo);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Result.fail(-1002, e.getMessage(), e);
    }
    return Result.succ(res);
  }

  @ApiOperation(value = "周二发版接口", httpMethod = "POST")
  @PostMapping(value = "/ci/flink/deployFirst")
  public Result<String> deployFirst(
      HttpServletRequest request,
      @Valid @RequestBody DeployTueDo deployTueDo,
      @RequestHeader("Alter-Token") String token) {
    String res = null;
    try {
      hdfsService.createHdfsDir(deployTueDo.getCreateDirDo());
      log.info("create hdfs dir success {}", deployTueDo.getCreateDirDo());
      //      hdfsService.uploadFlinkMultiBucket(deployTueDo.getUploadDataToMultiBucketDo());
      log.info(
          "upload cicd data to multibuckets success {}",
          deployTueDo.getUploadDataToMultiBucketDo());
      res = "deploy success";
    } catch (Exception e) {
      return Result.fail(-1002, e.getMessage(), e);
    }
    return Result.succ(res);
  }

  @ApiOperation(value = "周四发版接口", httpMethod = "POST")
  @PostMapping(value = "/ci/flink/deploySecond")
  public Result<String> deploySecond(
      HttpServletRequest request,
      @Valid @RequestBody DeployThuDo deployThuDo,
      @RequestHeader("Alter-Token") String token) {
    String res = null;
    try {
      hdfsService.copyDeployData(deployThuDo.getCopyDeployDataDo());
      log.info("copy data success {}", deployThuDo.getCopyDeployDataDo());
      hdfsService.delHdfsBucketDir(deployThuDo.getDelHdfsDirDo());
      log.info("del bucket success {}", deployThuDo.getDelHdfsDirDo());
      res = "all data copy new bucket success";
    } catch (Exception e) {
      return Result.fail(-1002, e.getMessage(), e);
    }
    return Result.succ(res);
  }
}