package com.satan.controller;

import com.satan.common.Result;
import com.satan.entity.*;
import com.satan.service.HdfsService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequestMapping("/api/alter/cicd/pack/hdfs/open/")
@RestController
@Slf4j
public class HdfsHandle {

  @Autowired HdfsService hdfsService;

  @ApiOperation(value = "根据bucket id列表创建bucket id文件夹", httpMethod = "POST")
  @PostMapping(value = "/ci/flink/create/hdfs/dir", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<String> createHdfsDir(HttpServletRequest request, @RequestBody @Valid CreateBucketDirectoriesDo createBucketDirectoriesDo, @RequestHeader("Alter-Token") String token) {

    String res = null;
    try {
      res = hdfsService.createHdfsDir(createBucketDirectoriesDo);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Result.fail(-1002, e.getMessage(), e);
    }
    return Result.succ(res);

  }

  @ApiOperation(value = "根据bucket id列表删除bucket id文件夹和文件夹内数据", httpMethod = "POST")
  @PostMapping(value = "/ci/flink/delete/hdfs/dir", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<String> deleteHdfsDir(HttpServletRequest request, @RequestBody @Valid DelBucketsDataDo delBucketsDataDo, @RequestHeader("Alter-Token") String token) {
    String res = null;
    try {
      res = hdfsService.delHdfsBucketDir(delBucketsDataDo);
      log.info("del buckets data from {} ", delBucketsDataDo);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Result.fail(-1002, e.getMessage(), e);
    }
    return Result.succ(res);
  }

  @ApiOperation(value = "上传CI/CD产物到多个bucket id", httpMethod = "POST")
  @PostMapping(value = "/ci/flink/upload/multiBuckets", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<String> uploadDataToMultiBuckets(HttpServletRequest request, @Valid @RequestBody UploadDataToMultiBucketsDo uploadDataToMultiBucketsDo, @RequestHeader("Alter-Token") String token) {
    String res = null;
    try {
//      res = hdfsService.uploadFlinkMultiBucket(uploadDataToMultiBucketsDo);
      log.info("upload ci-cd data params is {}", uploadDataToMultiBucketsDo);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Result.fail(-1002, e.getMessage(), e);
    }
    return Result.succ(res);
  }

  @ApiOperation(value = "周二发版接口", httpMethod = "POST")
  @PostMapping(value = "/ci/flink/deployOnTue")
  public Result<String> deployOnTue(HttpServletRequest request, @Valid @RequestBody DeployOnTueDo deployOnTueDo, @RequestHeader("Alter-Token") String token) {
    String res = null;
    try {
      hdfsService.createHdfsDir(deployOnTueDo.getCreateBucketDirectoriesDo());
      log.info("create hdfs dir success {}", deployOnTueDo.getCreateBucketDirectoriesDo());
//      hdfsService.uploadFlinkMultiBucket(deployOnTueDo.getUploadDataToMultiBucketsDo());
      log.info("upload ci-cd data to multiBuckets success {}", deployOnTueDo.getUploadDataToMultiBucketsDo());
      res = "deploy success";
    } catch (Exception e) {
      return Result.fail(-1002, e.getMessage(), e);
    }
    return Result.succ(res);
  }

  @ApiOperation(value = "周四发版接口", httpMethod = "POST")
  @PostMapping(value = "/ci/flink/deployOnThu")
  public Result<String> deployOnThu(HttpServletRequest request, @Valid @RequestBody DeployOnThuDo deployOnThuDo, @RequestHeader("Alter-Token") String token) {
    String res = null;
    try {
      hdfsService.copyDeployData(deployOnThuDo.getCopyDeployDataDo());
      log.info("copy data success {}", deployOnThuDo.getCopyDeployDataDo());
      hdfsService.delHdfsBucketDir(deployOnThuDo.getDelBucketsDataDo());
      log.info("del bucket success {}", deployOnThuDo.getDelBucketsDataDo());
      res = "all data move success";
    } catch (Exception e) {
      return Result.fail(-1002, e.getMessage(), e);
    }
    return Result.succ(res);
  }


}