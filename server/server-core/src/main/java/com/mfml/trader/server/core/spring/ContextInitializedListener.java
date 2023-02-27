package com.mfml.trader.server.core.spring;



import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author caozhou
 * @date 2022-11-24 15:25
 */
public class ContextInitializedListener implements ApplicationListener<ContextRefreshedEvent> {



    private final AtomicBoolean state = new AtomicBoolean(false);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!this.state.compareAndSet(false, true)) {
            return;
        }

        // 启动核心循环
    }
}
