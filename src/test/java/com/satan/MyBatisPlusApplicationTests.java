package com.satan;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.satan.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.satan.mapper.ComplexQueryMapper;
import com.satan.mode.User;

import java.util.List;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest()
public class MyBatisPlusApplicationTests {

  @Autowired private ComplexQueryMapper complexQueryMapper;
  @Autowired private UserMapper userMapper;

  @Test
  public void testMapper() {
    List<User> users = complexQueryMapper.QueryGame();
    log.info(String.valueOf(users.size()));
  }

  @Test
  public void testPage(){
    IPage<User> page = new Page<>();
    User user = userMapper.selectById(0);
    System.out.println(user);
  }
}
