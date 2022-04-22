package com.satan.controller;

import com.satan.common.Result;
import com.satan.entity.CreateDirDo;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/api/hdfs")
public class HdfsHandle {
    @ApiOperation(value="根据job_id 创建hdfs文件夹", httpMethod = "GET")
    @GetMapping(value = "/ci/flink/create/hdfs/dir", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<String> createHdfsDir(HttpServletRequest request, @RequestBody @Valid CreateDirDo createDirDo, @RequestHeader("Alter-Token") String token) {

        return null;
    }

}
