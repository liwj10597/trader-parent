package com.mfml.trader.server.core.client;

import com.google.common.collect.Lists;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.function.KeyRandomStrategy;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author caozhou
 * @date 2023-04-19 14:43
 */
@Slf4j
@Component
public class StreamClient {
    public static final List<String> accessTokens = Lists.newArrayList();

    @PostConstruct
    public void init() {
        log.info("accessTokens init start");
        // 读取到内存
        BufferedReader buffer = null;
        try {
            File file = new File("/home/work/server/accessTokens");
            buffer = new BufferedReader(new FileReader(file));
            String line;
            while ((line = buffer.readLine()) != null) {
                if (StringUtils.isNotBlank(line) && !accessTokens.contains(line)) {
                    accessTokens.add(line);
                }
            }
            log.info("accessTokens init end");
        } catch (Exception e) {
            log.warn("local warn", e);
        } finally {
            if (buffer != null) {
                try {
                    buffer.close();
                } catch (Exception ex) {

                }
            }
        }
    }

    @Bean
    public OpenAiStreamClient openAiStreamClient() {
        log.info("openAiStreamClient init start");
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(600, TimeUnit.SECONDS)
                .build();
        OpenAiStreamClient client = OpenAiStreamClient
                .builder()
                .apiKey(accessTokens)
                //自定义key使用策略 默认随机策略
                .keyStrategy(new KeyRandomStrategy())
                .okHttpClient(okHttpClient)
                .build();
        log.info("openAiStreamClient init end");
        return client;
    }
}
