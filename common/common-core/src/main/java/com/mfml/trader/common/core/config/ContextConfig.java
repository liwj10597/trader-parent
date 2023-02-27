package com.mfml.trader.common.core.config;

import com.mfml.trader.common.core.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author: caozhou
 * @create: 2022-07-15 18:46
 * @description:
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
public class ContextConfig {

    @Bean(name = "springContextUtil")
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }
}
