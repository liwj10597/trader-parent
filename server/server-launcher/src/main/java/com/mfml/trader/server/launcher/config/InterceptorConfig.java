package com.mfml.trader.server.launcher.config;

import com.mfml.trader.server.core.controller.interceptor.SecretInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 注册安全拦截器
 *
 * @EnableAspectJAutoProxy注解使用AspectJAutoProxyRegistrar对象自定义组件，并将相应的组件添加到IOC容器中
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public WebMvcConfigurer webMvcConfigurerAdapter(SecretInterceptor secretInterceptor) {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(secretInterceptor)
                        .addPathPatterns("/**")
                        .excludePathPatterns("/*.html")
                        .excludePathPatterns("/*.css")
                        .excludePathPatterns("/error")
                        .excludePathPatterns("/actuator/**")
                        .excludePathPatterns("/swagger-ui/**")
                        .excludePathPatterns("/swagger-resources/**")
                        .excludePathPatterns("/v2/api-docs")
                ;
            }
        };
    }
}
