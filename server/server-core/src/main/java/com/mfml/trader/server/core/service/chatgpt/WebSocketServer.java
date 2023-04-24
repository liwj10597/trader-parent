package com.mfml.trader.server.core.service.chatgpt;

import cn.hutool.json.JSONUtil;
import com.mfml.trader.common.core.utils.JsonUtils;
import com.mfml.trader.server.core.listener.LocalCache;
import com.mfml.trader.server.core.listener.OpenAIWebSocketEventSourceListener;
import com.mfml.trader.server.core.service.chatgpt.ro.StreamRo;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 描述：websocket 服务端
 *
 * @author https:www.unfbx.com
 * @date 2023-03-23
 */
@Slf4j
@Component
@ServerEndpoint("/websocket/{uid}")
public class WebSocketServer {

    private static OpenAiStreamClient openAiStreamClient;

    @Autowired
    public void setOrderService(OpenAiStreamClient openAiStreamClient) {
        this.openAiStreamClient = openAiStreamClient;
    }

    // 当前会话
    private Session session;
    // 用户id -目前是按浏览器随机生成
    private String uid;

    /**
     * 建立连接
     * @param session
     * @param uid
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("uid") String uid) {
        this.session = session;
        this.uid = uid;
        log.info("[连接ID:{}] 建立连接", this.uid);
    }

    /**
     * 断开连接
     */
    @OnClose
    public void onClose() {
        log.info("[连接ID:{}] 断开连接", uid);
    }

    /**
     * 发送错误
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("[连接ID:{}] 错误原因:{}", this.uid, error.getMessage());
        error.printStackTrace();
    }

    /**
     * 接收到客户端消息
     * @param obj
     */
    @OnMessage
    public void onMessage(String obj) {
        log.info("[连接ID:{}] 收到消息:{}", this.uid, obj);
        StreamRo ro = JsonUtils.parseObject(obj, StreamRo.class);
        String prompt = Optional.ofNullable(ro.getPrompt()).orElse("");
        String content = Optional.ofNullable(ro.getContent()).orElse("");
        String preResponse = Optional.ofNullable(ro.getPreResponse()).orElse("");
        Integer maxTokens = ro.getMaxTokens();
        Double temperature = ro.getTemperature();

        // 保证请求openai的内容不至于过长导致性能很差
        List<Message> messages = new ArrayList<>();
        String value = (String) LocalCache.CACHE.get(uid);
        if (StringUtils.isNotBlank(value)) {
            List<Message> cacheList = JSONUtil.toList(value, Message.class);
            int total = prompt.length() + preResponse.length() + content.length();
            int from = cacheList.size() - 1;
            for (int idx = from; idx >= 0; idx--) {
                Message msgObj = cacheList.get(idx);
                String c = msgObj.getContent();
                total = total + c.length();
                if (total <= 512) {
                    messages.add(0, msgObj);
                }
            }
        }
        if (messages.size() >= 10) {
            messages = messages.subList(messages.size() - 10, messages.size());
        }

        if (StringUtils.isNotBlank(prompt)) {
            Message promptMessage = Message.builder()
                    .content(prompt)
                    .role(Message.Role.SYSTEM)
                    .build();
            messages.add(0, promptMessage);
        }

        if (StringUtils.isNotBlank(preResponse)) {
            Message responseMessage = Message.builder()
                    .content(preResponse.replaceAll("\n", ""))
                    .role(Message.Role.ASSISTANT)
                    .build();
            messages.add(responseMessage);
        }
        Message currentMessage = Message.builder()
                .content(content.replaceAll("\n", ""))
                .role(Message.Role.USER)
                .build();
        messages.add(currentMessage);

        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .messages(messages)
                .maxTokens(maxTokens == null ? 2048 : maxTokens)
                .temperature(temperature == null ? 0.2 : temperature)
                .stream(true)
                .build();
        log.info("上下文{}", chatCompletion);
        openAiStreamClient.streamChatCompletion(chatCompletion, new OpenAIWebSocketEventSourceListener(this.session));
        LocalCache.CACHE.put(uid, JSONUtil.toJsonStr(messages.subList(1, messages.size())), LocalCache.TIMEOUT);
    }
}

