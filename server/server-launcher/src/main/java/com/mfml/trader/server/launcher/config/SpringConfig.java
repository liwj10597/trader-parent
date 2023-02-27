package com.mfml.trader.server.launcher.config;

import com.mfml.trader.server.core.spring.ContextInitializedListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author caozhou
 * @date 2022-11-16 10:14
 */
@Configuration
@EnableAspectJAutoProxy
public class SpringConfig {

    @Bean
    ContextInitializedListener contextInitializedListener() {
        return new ContextInitializedListener();
    }
}
