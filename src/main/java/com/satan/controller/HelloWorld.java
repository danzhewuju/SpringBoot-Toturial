package com.satan.controller;

import com.satan.mode.User;
import com.satan.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Past;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping(value = "/test")
@Api(tags = "接口测试")
public class HelloWorld {

  @Autowired
  TestService testService;

  @GetMapping("/hello")
  @ApiOperation(value = "hello 接口测试", httpMethod = "GET")
  public String hello() {
    return testService.test();
  }

  @GetMapping("/list")
  @ApiOperation(value = "get list 接口测试", httpMethod = "GET")
  public String inputList(
      @RequestParam(value = "head", required = false, defaultValue = "") String heads,
      @RequestParam(value = "tag", required = false, defaultValue = "") List<String> tags,
      @RequestParam(value = "message", required = false, defaultValue = "") String messages) {
    StringBuffer res = new StringBuffer();
    res.append(heads + ":");
    tags.forEach(
        t -> {
          res.append(" " + t);
        });
    res.append(messages);
    return res.toString();
  }

  @PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
  public String checkUser(@RequestBody @Valid User user) {
    String res;
    res = user.getName() + "say:  my name is " + user.getName();
    return res;
  }

  @GetMapping(value = "/map", produces = MediaType.APPLICATION_JSON_VALUE)
  public String testMaps(@RequestParam Map<String, Object> map) {
    StringBuilder sb = new StringBuilder();
    for (String key : map.keySet()) {
      sb.append(key + ":" + map.get(key) + "\n");
    }
    return sb.toString();
  }

  @GetMapping(value = "/date", produces = MediaType.APPLICATION_JSON_VALUE)
  public String testDate(
      @Past @RequestParam(value = "date", required = false, defaultValue = "")
          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
          Date date) {

    if (date != null) {
      String timeStamp =null;
      try{
        timeStamp = String.valueOf(date.getTime());
      } catch (IllegalArgumentException e){
        return "error: "+e.toString();
      }
      return timeStamp;
    } else {
      return "date is wrong!";
    }
  }
}
