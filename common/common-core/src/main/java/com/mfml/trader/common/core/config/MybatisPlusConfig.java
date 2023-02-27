package com.mfml.trader.common.core.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.mfml.trader.common.core.handler.DayTableNameHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author caozhou
 * @date 2022-11-25 19:35
 */
@Configuration
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        dynamicTableNameInnerInterceptor.setTableNameHandler(
                //可以传多个表名参数，指定哪些表使用MonthTableNameHandler处理表名称
                new DayTableNameHandler("whale_order", "whale_order_child")
        );
        //以拦截器的方式处理表名称
        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
        //可以传递多个拦截器，即：可以传递多个表名处理器TableNameHandler
        //interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
        return interceptor;
    }
}
