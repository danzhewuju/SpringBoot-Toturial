package com.satan.config;

import com.satan.utils.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfiguration {

  @Bean("taskExecutor")
  public Executor asyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(Constants.CORE_POOL_SIZE);
    executor.setMaxPoolSize(Constants.MAX_POOL_SIZE);
    executor.setQueueCapacity(Constants.QUEUE_CAPACITY);
    executor.setKeepAliveSeconds(Constants.KEEP_ALIVE_TIME);
    executor.setThreadNamePrefix(Constants.THREAD_NAME_PREFIX);
    executor.initialize();
    return executor;
  }
}
