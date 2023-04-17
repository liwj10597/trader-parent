package com.mfml.trader.server.core.controller.chatgpt;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.mfml.trader.common.core.annotation.ApiScan;
import com.mfml.trader.server.core.chatgpt.listener.LocalCache;
import com.mfml.trader.server.core.chatgpt.listener.OpenSSEEventSourceListener;
import com.mfml.trader.server.core.chatgpt.ro.StreamRo;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.exception.BaseException;
import com.unfbx.chatgpt.exception.CommonError;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * chatgpt 流式接口
 * @author caozhou
 * @date 2023-03-15 18:05
 */
@Slf4j
@ApiScan
@RestController
public class SSEController {

    @Resource
    OpenAiStreamClient openAiStreamClient;


    @GetMapping("stream")
    @CrossOrigin
    public SseEmitter stream(@RequestParam("content") String content,
                                 @RequestParam("prompt") String prompt,
                                 @RequestParam("max_tokens") Integer maxTokens,
                                 @RequestParam("temperature") Double temperature,
                                 @RequestParam("uid") String uid) throws IOException {
        SseEmitter sseEmitter = new SseEmitter(0L);
        if (StringUtils.isBlank(uid) || StringUtils.isBlank(content)) {
            throw new BaseException(CommonError.PARAM_ERROR);
        }

        List<Message> messages = new ArrayList<>();
        String messageContext = (String) LocalCache.CACHE.get(uid);
        if (StringUtils.isNotBlank(messageContext)) {
            messages = JSONUtil.toList(messageContext, Message.class);
            if (messages.size() >= 10) {
                messages = messages.subList(messages.size() - 10, messages.size());
            }
        }

        if (StringUtils.isNotBlank(prompt)) {
            Message promptMessage = Message.builder()
                    .content(prompt)
                    .role(Message.Role.SYSTEM)
                    .build();
            messages.add(0, promptMessage);
        }

        Message currentMessage = Message.builder()
                .content(content)
                .role(Message.Role.USER)
                .build();
        messages.add(currentMessage);

        sseEmitter.send(SseEmitter.event().id(uid).name("连接成功！！！！").data(LocalDateTime.now()).reconnectTime(3000));
        sseEmitter.onCompletion(() -> { log.info(LocalDateTime.now() + ", uid#" + uid + ", on completion"); });
        sseEmitter.onTimeout(() -> log.info(LocalDateTime.now() + ", uid#" + uid + ", on timeout#" + sseEmitter.getTimeout()));
        sseEmitter.onError(
                throwable -> {
                    try {
                        log.error(LocalDateTime.now() + ", uid#" + uid + ", on error#" + throwable.toString());
                        sseEmitter.send(SseEmitter.event().id(uid).name("发生异常！").data(throwable.getMessage()).reconnectTime(3000));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        OpenSSEEventSourceListener openAIEventSourceListener = new OpenSSEEventSourceListener(sseEmitter);
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .messages(messages)
                .maxTokens(maxTokens == null ? 2048 : maxTokens)
                .temperature(temperature == null ? 0.2 : temperature)
                .stream(true)
                .build();
        openAiStreamClient.streamChatCompletion(chatCompletion, openAIEventSourceListener);
        LocalCache.CACHE.put(uid, JSONUtil.toJsonStr(messages), LocalCache.TIMEOUT);
        return sseEmitter;
    }

    @PostMapping(value = "streamPost")
    @CrossOrigin
    @ResponseBody
    public SseEmitter streamPost(@RequestBody StreamRo ro) throws IOException {
        String uid = ro.getUid();
        String content = ro.getContent();
        String prompt = ro.getPrompt();
        Integer maxTokens = ro.getMaxTokens();
        Double temperature = ro.getTemperature();
        SseEmitter sseEmitter = new SseEmitter(0L);
        if (StringUtils.isBlank(uid) || StringUtils.isBlank(content)) {
            throw new BaseException(CommonError.PARAM_ERROR);
        }

        List<Message> messages = new ArrayList<>();
        String messageContext = (String) LocalCache.CACHE.get(uid);
        if (StringUtils.isNotBlank(messageContext)) {
            messages = JSONUtil.toList(messageContext, Message.class);
            if (messages.size() >= 10) {
                messages = messages.subList(messages.size() - 10, messages.size());
            }
        }


        if (StringUtils.isNotBlank(prompt)) {
            Message promptMessage = Message.builder()
                    .content(prompt)
                    .role(Message.Role.SYSTEM)
                    .build();
            messages.add(0, promptMessage);
        }

        Message currentMessage = Message.builder()
                .content(content)
                .role(Message.Role.USER)
                .build();
        messages.add(currentMessage);

        sseEmitter.send(SseEmitter.event().id(uid).name("连接成功！！！！").data(LocalDateTime.now()).reconnectTime(3000));
        sseEmitter.onCompletion(() -> { log.info(LocalDateTime.now() + ", uid#" + uid + ", on completion"); });
        sseEmitter.onTimeout(() -> log.info(LocalDateTime.now() + ", uid#" + uid + ", on timeout#" + sseEmitter.getTimeout()));
        sseEmitter.onError(
                throwable -> {
                    try {
                        log.error(LocalDateTime.now() + ", uid#" + uid + ", on error#" + throwable.toString());
                        sseEmitter.send(SseEmitter.event().id(uid).name("发生异常！").data(throwable.getMessage()).reconnectTime(3000));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        OpenSSEEventSourceListener openAIEventSourceListener = new OpenSSEEventSourceListener(sseEmitter);
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .messages(messages)
                .maxTokens(maxTokens == null ? 2048 : maxTokens)
                .temperature(temperature == null ? 0.2 : temperature)
                .stream(true)
                .build();
        openAiStreamClient.streamChatCompletion(chatCompletion, openAIEventSourceListener);
        LocalCache.CACHE.put(uid, JSONUtil.toJsonStr(messages), LocalCache.TIMEOUT);
        return sseEmitter;
    }
}
