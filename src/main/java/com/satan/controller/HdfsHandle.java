package com.satan.controller;

import com.satan.common.Result;
import com.satan.entity.CreateHdfsDirDo;
import com.satan.entity.DelHdfsDirDo;
import com.satan.service.HdfsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequestMapping("/api/hdfs")
@RestController
public class HdfsHandle {

  @Autowired HdfsService hdfsService;

  @ApiOperation(
      value = "根据job_id 创建hdfs文件夹",
      httpMethod = "POST",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PostMapping(value = "/ci/flink/create/hdfs/dir", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<String> createHdfsDir(
      HttpServletRequest request, @RequestBody @Valid CreateHdfsDirDo createHdfsDirDo) {

    try {
      Result<String> stringResult = hdfsService.hdfsCreateDir(createHdfsDirDo);
      return stringResult;
    } catch (Exception e) {
      e.printStackTrace();
      return Result.fail(e.getMessage());
    }
  }

  @PostMapping(value = "/ci/flink/delete/hdfs/dir", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<String> deleteHdfsDir(@RequestBody @Valid DelHdfsDirDo delHdfsDirDo) {
    Result<String> delResult = hdfsService.hdfsDeleteDir(delHdfsDirDo);
    return delResult;
  }
}
