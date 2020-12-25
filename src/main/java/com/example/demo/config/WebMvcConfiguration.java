package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //最小活跃的线程数
        executor.setCorePoolSize(10);
        //最大线程数
        executor.setMaxPoolSize(200);
        //只有当线程的队列数超过QueueCapacity才会创建新的线程
        executor.setQueueCapacity(1);
        executor.setThreadNamePrefix("mvc-task-executor-");
        executor.initialize();
        configurer.setTaskExecutor(executor);
    }
}
