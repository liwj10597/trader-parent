package com.mfml.trader.server.core.chatgpt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.mfml.trader.server.core.chatgpt.ro.AskRo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    public static final String url = "https://api.openai.com/v1/completions";
    public static final String urlChat = "https://api.openai.com/v1/chat/completions";
    public static final String accessToken = "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";

    /**
     * 请求结果示例
     * {
     *   "id": "cmpl-6uMBI8XuETmjgCfyOIG7AnkiNEUKp",
     *   "object": "text_completion",
     *   "created": 1678889652,
     *   "model": "text-davinci-003",
     *   "choices": [
     *     {
     *       "text": "\n\n我是一个学生。",
     *       "index": 0,
     *       "logprobs": null,
     *       "finish_reason": "stop"
     *     }
     *   ],
     *   "usage": {
     *     "prompt_tokens": 9,
     *     "completion_tokens": 12,
     *     "total_tokens": 21
     *   }
     * }
     * @param ro
     * @return
     */
    @Override
    public String ask(AskRo ro) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        headers.setAcceptCharset(Lists.newArrayList(Charsets.UTF_8));

        HashMap<String, Object> params = new HashMap<>();
        ro.setModel(StringUtils.isBlank(ro.getModel()) ? "text-davinci-003" : ro.getModel());
        params.put("model", ro.getModel());
        params.put("prompt", ro.getPrompt());
        params.put("max_tokens", 50);
        params.put("temperature", 0);

        StringBuffer buffer = new StringBuffer();
        String json = restTemplate.postForObject(url, new HttpEntity<>(params, headers), String.class);
        JSONArray choices = JSON.parseObject(json).getJSONArray("choices");
        for (int idx = 0; idx < choices.size(); idx++) {
            String line = choices.getJSONObject(idx).getString("text");
            buffer.append(line.replaceAll("\n", ""));
        }

        return buffer.toString();
    }

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
        headers.setBearerAuth(accessToken);
        headers.setAcceptCharset(Lists.newArrayList(Charsets.UTF_8));

        HashMap<String, Object> params = new HashMap<>();
        ro.setModel(StringUtils.isBlank(ro.getModel()) ? "gpt-3.5-turbo" : ro.getModel());
        params.put("model", ro.getModel());

        Map<String, String> map = new HashMap<>();
        map.put("role", "user");
        map.put("content", ro.getPrompt());
        List<Map<String, String>> messages = Lists.newArrayList();
        messages.add(map);
        params.put("messages", messages);

        StringBuffer buffer = new StringBuffer();
        String json = restTemplate.postForObject(urlChat, new HttpEntity<>(params, headers), String.class);
        JSONArray choices = JSON.parseObject(json).getJSONArray("choices");
        for (int idx = 0; idx < choices.size(); idx++) {
            String line = choices.getJSONObject(idx).getJSONObject("message").getString("content");
            buffer.append(line.replaceAll("\n", ""));
        }

        return buffer.toString();
    }
}
