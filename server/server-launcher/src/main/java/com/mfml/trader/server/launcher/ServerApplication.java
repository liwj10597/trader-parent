package com.mfml.trader.server.launcher;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: caozhou
 * @create: 2022-07-15 15:51
 * @description:
 */
@SpringBootApplication(scanBasePackages = {"com.mfml.trader.server","com.mfml.trader.common"})
@MapperScan(basePackages = {"com.mfml.trader.server.dao.mapper", "com.mfml.trader.common.dao.mapper"})
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
