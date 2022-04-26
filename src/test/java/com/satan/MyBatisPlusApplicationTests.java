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
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

  @Test
  public void TestAssert() {
    int a = 0;
    Student stu = null;
    Assert.notNull(stu, "不为空");
    System.out.println("after assert!!!");
  }

  @Test
  public void TestQueryJoin() {
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
    List<String> emails =
        new ArrayList<String>() {
          {
            add("danyuhao@qq.com");
            add("lvjin@qq.com");
          }
        };
    complexQuery.setEmails(emails);
    List<ComplexQueryResult> complexQueryResults =
        complexQueryMapper.QueryComplexJoin(complexQuery);
  }

  @Test
  public void testHdfsCopyDir() throws URISyntaxException, IOException, InterruptedException {
    Configuration configuration = new Configuration();
    configuration.set("dfs.client.use.datanode.hostname", "true");
    FileSystem fs = FileSystem.get(new URI("hdfs://feifish.site:9000"), configuration, "hadoop");
    FileUtil.copy(
        fs, new Path("/test/flink/1"), fs, new Path("/test/flink/7"), false, configuration);
 log.info("run success");
  }
}
