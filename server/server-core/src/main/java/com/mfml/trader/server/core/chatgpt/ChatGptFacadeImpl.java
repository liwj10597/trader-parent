package com.mfml.trader.server.core.chatgpt;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.mfml.trader.server.core.chatgpt.ro.AskRo;
import com.mfml.trader.server.core.chatgpt.ro.Messages;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author caozhou
 * @date 2023-03-15 17:24
 */
@Slf4j
@Service
public class ChatGptFacadeImpl implements ChatGptFacade {

    @Resource
    RestTemplate restTemplate;

    public static final String urlModels = "https://api.openai.com/v1/models";
    public static final String urlChat = "https://api.openai.com/v1/chat/completions";
    public static final List<String> accessTokens = Lists.newArrayList();


    /**
     * 请求结果示例
     * {
     *   "id": "chatcmpl-6uX17caKajqsvi9WT8NKtjkvvhh7C",
     *   "object": "chat.completion",
     *   "created": 1678931305,
     *   "model": "gpt-3.5-turbo-0301",
     *   "usage": {
     *     "prompt_tokens": 14,
     *     "completion_tokens": 146,
     *     "total_tokens": 160
     *   },
     *   "choices": [
     *     {
     *       "message": {
     *         "role": "assistant",
     *         "content": "\n\n为了纪念父亲节，小明画了一幅画送给他的爸爸，爸爸很感动，问小明：“这是谁画的？”小明说：“是我画的，爸爸。”爸爸说：“太棒了！这真的是你自己画的吗？我不敢相信！你的画得太好啦！”小明心满意足地说：“是啊，爸爸，我终于能够画得像一个六岁的孩子了！”"
     *       },
     *       "finish_reason": "stop",
     *       "index": 0
     *     }
     *   ]
     * }
     * @param ro
     * @return
     */
    @Override
    public String chatGPT(AskRo ro) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessTokens.get(RandomUtil.randomInt(accessTokens.size())));
        headers.setAcceptCharset(Lists.newArrayList(Charsets.UTF_8));

        HashMap<String, Object> params = new HashMap<>();
        ro.setModel(StringUtils.isBlank(ro.getModel()) ? "gpt-3.5-turbo" : ro.getModel());
        params.put("model", ro.getModel());

        List<Messages> msgList = ro.getMessages();
        List<Map<String, String>> messages = Lists.newArrayList();
        for (Messages m : msgList) {
            Map<String, String> map = new HashMap<>();
            map.put("user", m.getRole());
            map.put("content", m.getContent());
            messages.add(map);
        }
        params.put("messages", messages);
        params.put("max_tokens", ro.getMax_token());
        params.put("temperature", ro.getTemperature());
        params.put("stream", ro.getStream());
        ResponseEntity<String> exchange = restTemplate.exchange(urlChat, HttpMethod.POST, new HttpEntity<>(params, headers), String.class);
        return JSON.toJSONString(exchange);
    }

    @Override
    public String models() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessTokens.get(RandomUtil.randomInt(accessTokens.size())));
        headers.setAcceptCharset(Lists.newArrayList(Charsets.UTF_8));

        HashMap<String, Object> params = new HashMap<>();
        ResponseEntity<String> exchange = restTemplate.exchange(urlModels, HttpMethod.GET, new HttpEntity<>(params, headers), String.class);
        return JSON.toJSONString(exchange);
    }
}
