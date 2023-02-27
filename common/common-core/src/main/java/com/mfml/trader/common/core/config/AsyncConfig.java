package com.mfml.trader.common.core.config;

import cn.hutool.core.thread.NamedThreadFactory;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: caozhou
 * @create: 2022-07-16 17:18
 * @description: thread pool config
 */
@Component
@EnableAsync
@Slf4j
public class AsyncConfig implements AsyncConfigurer {

    @Bean(name = "scheduledThreadPoolExecutor")
    public ScheduledThreadPoolExecutor scheduledThreadPoolExecutor() {
        return new ScheduledThreadPoolExecutor(
                40,
                new NamedThreadFactory("scheduled-async", false),
                new WhaleDiscardPolicy("scheduled-async")
        );
    }

    @Bean(name = "callerRunsExecutor")
    public Executor callerRunsExecutor() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        threadPool.setCorePoolSize(100);
        threadPool.setMaxPoolSize(500);
        threadPool.setQueueCapacity(30000);
        threadPool.setRejectedExecutionHandler(new WhaleCallerRunsPolicy("callerRuns-async"));
        threadPool.setWaitForTasksToCompleteOnShutdown(true);
        threadPool.setAwaitTerminationSeconds(60 * 15);
        threadPool.setThreadNamePrefix("callerRuns-async");
        threadPool.initialize();
        return threadPool;
    }

    @Bean(name = "discardExecutor")
    public Executor discardExecutor() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        threadPool.setCorePoolSize(30);
        threadPool.setMaxPoolSize(200);
        threadPool.setQueueCapacity(30000);
        threadPool.setRejectedExecutionHandler(new WhaleDiscardPolicy("discard-async"));
        threadPool.setWaitForTasksToCompleteOnShutdown(true);
        threadPool.setAwaitTerminationSeconds(60 * 15);
        threadPool.setThreadNamePrefix("discard-async");
        threadPool.initialize();
        return threadPool;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class WhaleDiscardPolicy implements RejectedExecutionHandler {

        private String threadFactoryName;

        /**
         * Does nothing, which has the effect of discarding task r.
         *
         * @param r the runnable task requested to be executed
         * @param e the executor attempting to execute this task
         */
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            String msg = String.format("Thread pool [%s] EXHAUSTED, task is discarded, please check!" +
                            " Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d (completed: %d)," +
                            " Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s)",
                    threadFactoryName, e.getPoolSize(), e.getActiveCount(), e.getCorePoolSize(), e.getMaximumPoolSize(), e.getLargestPoolSize(),
                    e.getTaskCount(), e.getCompletedTaskCount(), e.isShutdown(), e.isTerminated(), e.isTerminating());
            log.warn(msg);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class WhaleCallerRunsPolicy implements RejectedExecutionHandler {

        private String threadFactoryName;

        /**
         * Executes task r in the caller's thread, unless the executor
         * has been shut down, in which case the task is discarded.
         *
         * @param r the runnable task requested to be executed
         * @param e the executor attempting to execute this task
         */
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            String msg = String.format("Thread pool [%s] EXHAUSTED, caller runs task, please check!" +
                            " Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d (completed: %d)," +
                            " Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s)",
                    threadFactoryName, e.getPoolSize(), e.getActiveCount(), e.getCorePoolSize(), e.getMaximumPoolSize(), e.getLargestPoolSize(),
                    e.getTaskCount(), e.getCompletedTaskCount(), e.isShutdown(), e.isTerminated(), e.isTerminating());
            log.warn(msg);
            if (!e.isShutdown()) {
                r.run();
            }
        }
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }

    /**
     * 自定义异常处理类
     */
    class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
            log.error("async fail, method:{} params:{}", method.getName(), Arrays.toString(obj), throwable);
        }
    }
}
