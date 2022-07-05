package com.satan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableAsync(proxyTargetClass = true)
@EnableScheduling
public class SpringbootApplication {
  public static void main(String[] args) {
    SpringApplication.run(SpringbootApplication.class, args);
  }
}

