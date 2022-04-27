package com.satan.controller;

import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Api(tags = "健康检查", produces = MediaType.APPLICATION_JSON_VALUE)
public class HealthCheckController {

    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public String healthCheck() {
        return "OK";
    }
}
