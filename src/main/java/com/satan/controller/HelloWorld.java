package com.satan.controller;

import com.satan.mode.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/test")
@Api(tags = "接口测试")
public class HelloWorld {
  @GetMapping("/hello")
  @ApiOperation(value = "hello 接口测试", httpMethod = "GET")
  public String hello() {
    return "hello world";
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
  public String testMaps(@RequestParam Map<String, Object>  map){
    StringBuilder sb = new StringBuilder();
    for(String key:map.keySet()){
      sb.append(key+":"+map.get(key)+"\n");
    }
    return sb.toString();
  }
}
