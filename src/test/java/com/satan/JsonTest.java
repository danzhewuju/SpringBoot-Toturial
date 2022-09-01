package com.satan;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest()
public class JsonTest {
    @Test
    public void ParseJson(){
        Map<String, String> res = new HashMap<>();
        res.put("1", "2");
        res.put("2", "3");
        log.info(res.toString());
    }
}
