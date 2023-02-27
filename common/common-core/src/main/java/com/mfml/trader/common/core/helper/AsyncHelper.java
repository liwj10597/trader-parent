package com.mfml.trader.common.core.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * 异步助手
 *
 * @author: caozhou
 * @create: 2022-07-16 23:23
 */
@Component
@Slf4j
public class AsyncHelper {

    @Resource(name = "scheduledThreadPoolExecutor")
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    public void schedule(Runnable runnable, long delay, TimeUnit unit) {
        scheduledThreadPoolExecutor.schedule(runnable, delay, unit);
    }

    public <T> ScheduledFuture<T> schedule(Callable<T> callable, long delay, TimeUnit unit) {
        return scheduledThreadPoolExecutor.schedule(callable, delay, unit);
    }

    @Async("callerRunsExecutor")
    public <T> CompletableFuture<T> executeBlocking(Supplier<CompletableFuture<T>> supplier) {
        return supplier.get();
    }

    @Async("callerRunsExecutor")
    public void executeBlocking(Runnable runnable) {
        runnable.run();
    }

    @Async("discardExecutor")
    public <T> CompletableFuture<T> executeNoBlocking(Supplier<CompletableFuture<T>> supplier) {
        return supplier.get();
    }

    @Async("discardExecutor")
    public void executeNoBlocking(Runnable runnable) {
        runnable.run();
    }

}
