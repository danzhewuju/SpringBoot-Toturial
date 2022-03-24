package com.satan.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User {

  private int id;
  private String name;
  private int age;
  private String email;
}
