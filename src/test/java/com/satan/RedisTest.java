package com.satan;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: YuHao
 * @Date: 2022/12/8 19:21
 */

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest()
public class RedisTest {
    RedissonClient redissonClient;

    public RedissonClient getRedissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://feifish.site:6379");
        redissonClient = Redisson.create(config);
        return redissonClient;
    }

    public Boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) {
        RLock lock =redissonClient.getLock(lockKey);
        try {
            boolean locked = lock.tryLock(waitTime, leaseTime, unit);
            return locked;
        } catch (InterruptedException | CancellationException e) {
            log.error("尝试获取锁 {} 失败", lockKey);
        }
        return Boolean.FALSE;
    }

    @Test
    public void testRedis() throws InterruptedException {
        RedissonClient redissonClient = getRedissonClient();
        redissonClient.getConfig().toString();
        String lockKey = "redis-test";
        for (int i = 0; i < 30; i++) {
            new Thread(()->{
                log.info("this is thread : {}", Thread.currentThread().getName());
                if (tryLock(lockKey, 20, 10, TimeUnit.SECONDS)){
                    log.info("线程:{}, 获取锁成功", Thread.currentThread().getName());
                }else {
                    log.info("线程:{}, 获取锁失败", Thread.currentThread().getName());
                }
            }).start();

        }
        Thread.sleep(24000);
        log.info("主进程完成！");


    }

}
