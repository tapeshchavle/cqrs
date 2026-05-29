package com.cqrs.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfiguration {

    private static final Logger log = LoggerFactory.getLogger(AsyncConfiguration.class);

    @Value("${cqrs.async.core-pool-size:4}")
    private int corePoolSize;

    @Value("${cqrs.async.max-pool-size:8}")
    private int maxPoolSize;

    @Value("${cqrs.async.queue-capacity:100}")
    private int queueCapacity;

    @Bean(name = "cqrsEventExecutor")
    public Executor cqrsEventExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("cqrs-event-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        log.info("CQRS event executor initialized: core={}, max={}, queue={}", corePoolSize, maxPoolSize, queueCapacity);
        return executor;
    }
}
