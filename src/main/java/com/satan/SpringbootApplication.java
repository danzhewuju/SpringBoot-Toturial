package com.satan;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.satan.mode.User;
import com.satan.service.LoadFile;
import org.apache.flink.util.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableSwagger2
@EnableAsync(proxyTargetClass = true)
@EnableScheduling
public class SpringbootApplication {

  public String getPath(){
    return Thread.currentThread().getContextClassLoader().getResource("/_metadata").toString();
  }

  public static void main(String[] args) throws IOException {
    SpringApplication.run(SpringbootApplication.class, args);

    SpringbootApplication springbootApplication = new SpringbootApplication();
    String path = springbootApplication.getPath();
    System.out.println(path);
    if (new File(path).exists()){
      System.out.println("文件存在！！");
    }else {
      System.out.println("文件不存在！！");
    }

  }
}

