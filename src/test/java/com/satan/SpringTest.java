package com.satan;


import com.satan.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest()
public class SpringTest {

    @Resource
    TestService testService;

    @Test
    public void serviceTest() {
        String path = "viewfs://jssz-bigdata-cluster/realtime/flink/checkpoint/state/8605f8ef60054fa39adaeb50a0e05ca9" +
                      "/ed7c43b7fa8e23334a126e52d27";
        String new_path = path.replace("checkpoint", "checkpoint/trash");
        System.out.println(new_path);
    }

}
