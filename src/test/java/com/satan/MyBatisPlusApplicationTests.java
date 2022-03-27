package com.satan;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carrotsearch.sizeof.RamUsageEstimator;
import com.satan.mapper.ComplexQueryMapper;
import com.satan.mapper.UserMapper;
import com.satan.mode.ComplexQuery;
import com.satan.mode.ComplexQueryResult;
import com.satan.mode.Student;
import com.satan.mode.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
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
  public void testPage() {
    IPage<User> page = new Page<>();
    User user = userMapper.selectById(0);
    System.out.println(user);
  }

  @Test
  public void TestStudent() {
    Student student = new Student();
    int[] a = {1, 2};

    long l = RamUsageEstimator.sizeOfAll(student);
    log.info("{}", l);
    List<String> names = null;
    System.out.println("No");
  }

  @Test
  public void TestMapper() {
    ComplexQuery complexQuery = new ComplexQuery();
    complexQuery.setType("User");
    List<Integer> data =
        new ArrayList<Integer>() {
          {
            add(0);
            add(1);
          }
        };
    complexQuery.setIds(data);

    List<User> users = complexQueryMapper.QueryUsers(complexQuery);
    System.out.println(users.size());
  }

  @Test
  public void TestComplexMapper() {
    ComplexQuery complexQuery = new ComplexQuery();
    complexQuery.setType("User");
    List<Integer> data =
        new ArrayList<Integer>() {
          {
            add(0);
            add(1);
          }
        };
    complexQuery.setIds(data);

    List<ComplexQueryResult> complexQueryResults = complexQueryMapper.QueryComplex(complexQuery);
    System.out.println(complexQueryResults.size());
    log.info("{}", complexQueryResults);
  }
}
