package com.mfml.trader.common.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author caozhou
 * @date 2022-11-24 16:40
 */
@Configuration
@EnableScheduling
public class ScheduleConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }
    @Bean
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(10, (Runnable r) -> {
            Thread thread = new Thread(r);
            thread.setName("定时任务线程池");
            return thread;
        });
    }
}
