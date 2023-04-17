package com.mfml.trader.server.core.chatgpt;

import cn.hutool.json.JSONUtil;
import com.mfml.trader.common.core.utils.JsonUtils;
import com.mfml.trader.server.core.chatgpt.listener.LocalCache;
import com.mfml.trader.server.core.chatgpt.listener.OpenAIWebSocketEventSourceListener;
import com.mfml.trader.server.core.chatgpt.ro.StreamRo;
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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

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

    //当前会话
    private Session session;
    //用户id -目前是按浏览器随机生成
    private String uid;

    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

    /**
     * 用来存放每个客户端对应的WebSocketServer对象
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap();

    /**
     * 为了保存在线用户信息，在方法中新建一个list存储一下【实际项目依据复杂度，可以存储到数据库或者缓存】
     */
    private final static List<Session> SESSIONS = Collections.synchronizedList(new ArrayList<>());


    /**
     * 建立连接
     * @param session
     * @param uid
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("uid") String uid) {
        this.session = session;
        this.uid = uid;
        webSocketSet.add(this);
        SESSIONS.add(session);
        if (webSocketMap.containsKey(uid)) {
            webSocketMap.remove(uid);
            webSocketMap.put(uid, this);
        } else {
            webSocketMap.put(uid, this);
        }
        log.info("[连接ID:{}] 建立连接, 当前连接数:{}", this.uid);
    }

    /**
     * 断开连接
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        if (webSocketMap.containsKey(uid)) {
            webSocketMap.remove(uid);
        }
        log.info("[连接ID:{}] 断开连接, 当前连接数:{}", uid);
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
        String prompt = ro.getPrompt();
        String content = ro.getContent();
        Integer maxTokens = ro.getMaxTokens();
        Double temperature = ro.getTemperature();

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

        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .messages(messages)
                .maxTokens(maxTokens == null ? 2048 : maxTokens)
                .temperature(temperature == null ? 0.2 : temperature)
                .stream(true)
                .build();
        openAiStreamClient.streamChatCompletion(chatCompletion, new OpenAIWebSocketEventSourceListener(this.session));
        LocalCache.CACHE.put(uid, JSONUtil.toJsonStr(messages), LocalCache.TIMEOUT);
    }
}

