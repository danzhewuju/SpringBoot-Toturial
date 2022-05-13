package com.satan.controller;

import com.satan.common.Result;
import com.satan.entity.*;
import com.satan.service.HdfsService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

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
  public Result<String> uploadDataToMultiBuckets(HttpServletRequest request, @RequestBody @Valid DeployGrayReleaseDo deployGrayReleaseDo, @RequestHeader("Alter-Token") String token) {
    String res = null;
    try {
      hdfsService.uploadFlinkToMultiBuckets(deployGrayReleaseDo);
      log.info("upload ci-cd data params is {}", deployGrayReleaseDo);
      res = "upload ci-cd data params is " + deployGrayReleaseDo;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Result.fail(-1002, e.getMessage(), e);
    }
    return Result.succ(res);
  }

  @ApiOperation(value = "灰度发布接口", httpMethod = "POST")
  @PostMapping(value = "/ci/flink/deployGrayRelease", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<String> deployGrayRelease(HttpServletRequest request, @RequestBody @Valid DeployGrayReleaseDo deployGrayReleaseDo, @RequestHeader("Alter-Token") String token) {
    String res = null;
    try {
      boolean finished = hdfsService.setLastGrayDeployVersion(deployGrayReleaseDo.getTargetFlinkVersion(), deployGrayReleaseDo.getSourceFlinkVersion());
      if (finished) {
        hdfsService.uploadFlinkToMultiBuckets(deployGrayReleaseDo);
        log.info("upload ci-cd data to multiBuckets success {}", deployGrayReleaseDo);
        res = "deploy success";
        return Result.succ(res);
      } else {
        res = "there is an error when writing gray version temporary file.";
        return Result.fail(-1002, res, null);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Result.fail(-1002, e.getMessage(), e);
    }
  }

  @ApiOperation(value = "全量发布接口", httpMethod = "POST")
  @PostMapping(value = "/ci/flink/deployFullRelease", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<String> deployFullRelease(HttpServletRequest request, @RequestBody @Valid DeployFullReleaseDo deployFullReleaseDo, @RequestHeader("Alter-Token") String token) {
    String res = null;
    try {
      String lastGrayDeployVersion = hdfsService.pollLastGrayDeployVersion(deployFullReleaseDo.getRandomCopySingleBucketDataDo().getSourceFlinkVersion());
      if (StringUtils.isNotEmpty(lastGrayDeployVersion)) {
        deployFullReleaseDo.getRandomCopySingleBucketDataDo().setTargetFlinkVersion(lastGrayDeployVersion);
        hdfsService.copySingleBucketDataToBase(deployFullReleaseDo.getRandomCopySingleBucketDataDo());
        log.info("copy data success {}", deployFullReleaseDo.getRandomCopySingleBucketDataDo());
        hdfsService.deleteMultiBucketDirectories(deployFullReleaseDo.getDelBucketsDataDo());
        log.info("del bucket success {}", deployFullReleaseDo.getDelBucketsDataDo());
        res = "all data move success";
      } else {
        res = "there is no last gray version, so no data to move";
      }
    } catch (Exception e) {
      return Result.fail(-1002, e.getMessage(), e);
    }
    return Result.succ(res);
  }

  @ApiOperation(value = "紧急发版接口", httpMethod = "POST")
  @PostMapping(value = "/ci/flink/hotFixRelease", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<String> deployHotFixRelease(HttpServletRequest request, @RequestBody @Valid DeployHotFixReleaseDo deployHotFixReleaseDo, @RequestHeader("Alter-Token") String token) {
    String res = null;
    try {
      hdfsService.pollLastGrayDeployVersion(deployHotFixReleaseDo.getFlinkVersion());
      hdfsService.deployHotfixRelease(deployHotFixReleaseDo);
      res = "hotfix release success";
      return Result.succ(res);
    } catch (Exception e) {
      return Result.fail(-1002, e.getMessage(), e);
    }
  }

  @ApiOperation(value="获取上一次灰度发版随机bucket-id列表",httpMethod="GET")
  @GetMapping(value = "/ci/flink/get/gray/buckets/list", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<List<String>> getGrayBucketsList(HttpServletRequest request, @RequestParam(value = "flinkVersion", required = true) String flinkVersion, @RequestHeader("Alter-Token") String token) {
    List<String> res = null;
    try {
      List<String> randomBucketsList = hdfsService.getRandomBucketsList(flinkVersion);
      return Result.succ(randomBucketsList);
    } catch (Exception e) {
      e.printStackTrace();
      return Result.fail(-1002, e.getMessage(), e);
    }
  }


}