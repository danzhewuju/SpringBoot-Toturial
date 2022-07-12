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
        log.info(testService.test());
    }
}
