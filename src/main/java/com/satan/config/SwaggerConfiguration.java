package com.satan.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableAutoConfiguration
@EnableSwagger2
public class SwaggerConfiguration {

  @Bean
  public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.satan.controller"))
        .paths(PathSelectors.any())
        .build();
  }

  // 基本信息的配置，信息会在api文档上显示
  private ApiInfo apiInfo() {
    return new ApiInfoBuilder().title("测试的接口文档").description("API相关接口的文档").version("1.0").build();
  }
}
