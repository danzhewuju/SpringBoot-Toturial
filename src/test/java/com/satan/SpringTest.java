package com.satan;


import com.satan.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
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
        String tag = "FLINK-1.11.12.123";
        if (tag.matches("FLINK-(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)")) {
            log.info("match");
            int res = Integer.parseInt(tag.substring(tag.lastIndexOf(".") + 1));
            log.info("res:{}", res);
            String preKey = tag.substring(0, tag.lastIndexOf("."));
            log.info("preKey:{}", preKey);
        } else {
            log.info("don't match");
        }

    }

}
