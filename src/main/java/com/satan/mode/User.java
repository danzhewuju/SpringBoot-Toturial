package com.satan.mode;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class User {
  private Integer id;

  @NotBlank(message = "name不为空")
  private String name;

  private int age;
  private String email;
}
