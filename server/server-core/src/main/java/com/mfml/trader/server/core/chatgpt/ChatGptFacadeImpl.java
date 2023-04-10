package com.mfml.trader.server.core.chatgpt;

import cn.hutool.core.util.RandomUtil;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mfml.trader.server.core.chatgpt.ro.AskRo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

    @PostConstruct
    public void init() {
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
    public Object chatGPT(AskRo ro) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessTokens.get(RandomUtil.randomInt(accessTokens.size())));
            headers.setAcceptCharset(Lists.newArrayList(Charsets.UTF_8));

            HashMap<String, Object> params = new HashMap<>();
            ro.setModel(StringUtils.isBlank(ro.getModel()) ? "gpt-3.5-turbo" : ro.getModel());
            params.put("model", ro.getModel());
            params.put("messages", ro.getMessages());
            Integer maxToken = ro.getMax_token();
            if (null != maxToken) {
                params.put("max_tokens", maxToken);
            }
            Double temperature = ro.getTemperature();
            if (null != temperature) {
                params.put("temperature", temperature);
            }
            Boolean stream = ro.getStream();
            if (null != stream) {
                params.put("stream", stream);
            }
            ResponseEntity<String> exchange = restTemplate.exchange(urlChat, HttpMethod.POST, new HttpEntity<>(params, headers), String.class);
            return exchange.getBody();
        } catch (Exception e) {
            Map<String, Object> data = Maps.newLinkedHashMap();
            data.put("error", e.getMessage());
            return data;
        }
    }

    @Override
    public Object models() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessTokens.get(RandomUtil.randomInt(accessTokens.size())));
            headers.setAcceptCharset(Lists.newArrayList(Charsets.UTF_8));

            HashMap<String, Object> params = new HashMap<>();
            ResponseEntity<String> exchange = restTemplate.exchange(urlModels, HttpMethod.GET, new HttpEntity<>(params, headers), String.class);
            return exchange.getBody();
        } catch (Exception e) {
            Map<String, Object> data = Maps.newLinkedHashMap();
            data.put("error", e.getMessage());
            return data;
        }
    }
}
