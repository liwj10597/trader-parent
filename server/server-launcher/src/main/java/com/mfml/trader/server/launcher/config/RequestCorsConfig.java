package com.mfml.trader.server.launcher.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Copyright (c) 2018-2020 NCARZONE INFORMATION TECHNOLOGY CO.LTD.
 * All rights reserved.
 * This software is the confidential and proprietary information of NCARZONE
 * INFORMATION Technology CO.LTD("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with NCARZONE.
 * Created by chauncey on 2023/4/13.
 */
@Configuration
public class RequestCorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 解决request访问时的跨域问题：No 'Access-Control-Allow-Origin' header is present on the
        // requested resource.
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "PUT", "POST","HEAD","DELETE","OPTIONS")
                .allowCredentials(true).maxAge(3600)
                .allowedHeaders("*");

    }
}
