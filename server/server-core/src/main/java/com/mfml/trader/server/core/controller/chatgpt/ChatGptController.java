package com.mfml.trader.server.core.controller.chatgpt;

import com.google.common.collect.Lists;
import com.mfml.trader.common.core.annotation.ApiScan;
import com.mfml.trader.common.core.result.Result;
import com.mfml.trader.common.core.result.ResultUtil;
import com.mfml.trader.server.core.service.chatgpt.ChatGptFacade;
import com.mfml.trader.server.core.listener.OpenSSEEventSourceListener;
import com.mfml.trader.server.core.service.chatgpt.ro.AskRo;
import com.mfml.trader.server.core.service.chatgpt.ro.Messages;
import com.mfml.trader.server.core.client.StreamClient;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.chat.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author caozhou
 * @date 2023-03-15 18:05
 */
@Slf4j
@RestController
public class ChatGptController {

    @Resource
    ChatGptFacade chatGptFacade;

    @Resource
    OpenAiStreamClient openAiStreamClient;


    @RequestMapping(value="/models",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Object models() {
        return chatGptFacade.models();
    }

    @PostMapping(value = "chatGPT")
    @ResponseBody
    public Object chatGPT(@RequestBody AskRo ro) {
        return chatGptFacade.chatGPT(ro);
    }

    @PostMapping(value = "addToken")
    public Result<Boolean> addToken(@RequestParam(value = "key") String key) {
        if (StringUtils.isBlank(key)) {
            return ResultUtil.success(true);
        }
        try {
            // 写入文件
            FileWriter writer = new FileWriter(new File("/home/work/server/accessTokens"));
            BufferedWriter buffer = new BufferedWriter(writer);
            buffer.write(key);
            buffer.close();
            writer.close();

            // 写入内存
            if (!StreamClient.accessTokens.contains(key)) {
                StreamClient.accessTokens.add(key);
            }
        } catch (Exception e) {
            log.warn("addToken warn ", e);
            return ResultUtil.success(false);
        }

        return ResultUtil.success(true);
    }

    @PostMapping(value = "getToken")
    public Result<List<String>> getToken() {
        return ResultUtil.success(StreamClient.accessTokens);
    }

    @PostMapping(value = "cleanToken")
    public Result<Boolean> deleteToken(@RequestParam(value = "key") String key) {
        StreamClient.accessTokens.clear();
        return ResultUtil.success(true);
    }


    @PostMapping(value = "streamGPT")
    @CrossOrigin
    @ResponseBody
    public SseEmitter streamGPT(@RequestBody AskRo ro)  throws IOException{
        //默认30秒超时,设置为0L则永不超时
        SseEmitter sseEmitter = new SseEmitter(0l);
//        if (StrUtil.isBlank(uid)) {
//            throw new BaseException(CommonError.SYS_ERROR);
//        }
//        String messageContext = (String) LocalCache.CACHE.get(uid);
        String uid = Calendar.getInstance().getTime().toString();
        List<Message> messages = Lists.newLinkedList();
        if (!CollectionUtils.isEmpty(ro.getMessages())) {
            for(Messages msg : ro.getMessages()){
                Message currentMessage = Message.builder()
                .content(msg.getContent())
                .role(msg.getRole().equals("system")?Message.Role.SYSTEM:(msg.getRole().equals("user")?Message.Role.USER:Message.Role.ASSISTANT))
                .build();
                messages.add(currentMessage);
            }
        }
        sseEmitter.send(SseEmitter.event().id(uid).name("连接成功！！！！").data(LocalDateTime.now()).reconnectTime(3000));
        sseEmitter.onCompletion(() -> {
            log.info(LocalDateTime.now() + ", uid#"+uid+" , on completion");
        });
        sseEmitter.onTimeout(() -> log.info(LocalDateTime.now() + ", uid#" + uid + ", on timeout#" + sseEmitter.getTimeout()));
        sseEmitter.onError(
                throwable -> {
                    try {
                        log.info(LocalDateTime.now() + ", uid#" + uid + ", on error#" + throwable.toString());
                        sseEmitter.send(SseEmitter.event().id(uid).name("发生异常！").data(throwable.getMessage()).reconnectTime(3000));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        OpenSSEEventSourceListener openAIEventSourceListener = new OpenSSEEventSourceListener(sseEmitter);
        openAiStreamClient.streamChatCompletion(messages, openAIEventSourceListener);
//        LocalCache.CACHE.put(uid, JSONUtil.toJsonStr(messages), LocalCache.TIMEOUT);
        return sseEmitter;
    }

    @GetMapping("streamTest")
    @CrossOrigin
    public SseEmitter streamTest(@RequestParam("message") String msg/*, @RequestHeader Map<String, String> headers*/) throws IOException {
        //默认30秒超时,设置为0L则永不超时
        SseEmitter sseEmitter = new SseEmitter(0l);
        /*String uid = headers.get("uid");
        if (StrUtil.isBlank(uid)) {
            throw new BaseException(CommonError.SYS_ERROR);
        }*/

        String uid = Calendar.getInstance().getTime().toString();
//        String messageContext = (String) LocalCache.CACHE.get(uid);
        List<Message> messages = new ArrayList<>();
//        if (StrUtil.isNotBlank(messageContext)) {
//            messages = JSONUtil.toList(messageContext, Message.class);
//            if (messages.size() >= 10) {
//                messages = messages.subList(1, 10);
//            }
//            Message currentMessage = Message.builder().content(msg).role(Message.Role.USER).build();
//            messages.add(currentMessage);
//        } else {
            Message currentMessage = Message.builder().content(msg).role(Message.Role.USER).build();
            messages.add(currentMessage);
//        }
        sseEmitter.send(SseEmitter.event().id(uid).name("连接成功！！！！").data(LocalDateTime.now()).reconnectTime(3000));
        sseEmitter.onCompletion(() -> {
            log.info(LocalDateTime.now() + ", uid#" + uid + ", on completion");
        });
        sseEmitter.onTimeout(() -> log.info("streamTest:"+LocalDateTime.now() + ", uid#" + uid + ", on timeout#" + sseEmitter.getTimeout()));
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
        openAiStreamClient.streamChatCompletion(messages, openAIEventSourceListener);
//        LocalCache.CACHE.put(uid, JSONUtil.toJsonStr(messages), LocalCache.TIMEOUT);
        return sseEmitter;
    }


}
