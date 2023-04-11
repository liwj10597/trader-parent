package com.mfml.trader.server.launcher;

import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.function.KeyRandomStrategy;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: caozhou
 * @create: 2022-07-15 15:51
 * @description:
 */
@SpringBootApplication(scanBasePackages = {"com.mfml.trader.server","com.mfml.trader.common"})
@MapperScan(basePackages = {"com.mfml.trader.server.dao.mapper", "com.mfml.trader.common.dao.mapper"})
public class ServerApplication {

    @Value("${chatgpt.apiKey}")
    private List<String> apiKey;

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    public OpenAiStreamClient openAiStreamClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(600, TimeUnit.SECONDS)
                .build();
        return OpenAiStreamClient
                .builder()
                .apiKey(apiKey)
                //自定义key使用策略 默认随机策略
                .keyStrategy(new KeyRandomStrategy())
                .okHttpClient(okHttpClient)
                .build();
    }
}
