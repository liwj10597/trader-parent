package com.mfml.trader.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: caozhou
 * @create: 2022-07-16 23:56
 * @description:
 */
@SpringBootApplication(scanBasePackages = {"com.mfml.trader.server","com.mfml.trader.common"})
@MapperScan(basePackages = {"com.mfml.whale.server.dao.mapper", "com.mfml.whale.common.dao.mapper"})
public class ServerTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerTestApplication.class, args);
    }
}
