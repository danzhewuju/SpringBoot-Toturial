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
      res = hdfsService.createMultiBucketDirectories(createBucketDirectoriesDo);
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
      res = hdfsService.deleteMultiBucketDirectories(delBucketsDataDo);
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

  @ApiOperation(value = "灰度发布接口", httpMethod = "POST")
  @PostMapping(value = "/ci/flink/deployGrayRelease", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<String> deployGrayRelease(HttpServletRequest request, @RequestBody @Valid UploadDataToMultiBucketsDo uploadDataToMultiBucketsDo, @RequestHeader("Alter-Token") String token) {
    String res = null;
    try {
      hdfsService.uploadFlinkToMultiBuckets(uploadDataToMultiBucketsDo);
      log.info("upload ci-cd data to multiBuckets success {}", uploadDataToMultiBucketsDo);
      res = "deploy success";
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Result.fail(-1002, e.getMessage(), e);
    }
    return Result.succ(res);
  }

  @ApiOperation(value = "全量发布接口", httpMethod = "POST")
  @PostMapping(value = "/ci/flink/deployFullRelease", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<String> deployFullRelease(HttpServletRequest request, @RequestBody @Valid DeployFullReleaseDo deployFullReleaseDo, @RequestHeader("Alter-Token") String token) {
    String res = null;
    try {
      hdfsService.copySingleBucketDataToBase(deployFullReleaseDo.getRandomCopySingleBucketDataDo());
      log.info("copy data success {}", deployFullReleaseDo.getRandomCopySingleBucketDataDo());
      hdfsService.deleteMultiBucketDirectories(deployFullReleaseDo.getDelBucketsDataDo());
      log.info("del bucket success {}", deployFullReleaseDo.getDelBucketsDataDo());
      res = "all data move success";
    } catch (Exception e) {
      return Result.fail(-1002, e.getMessage(), e);
    }
    return Result.succ(res);
  }


}