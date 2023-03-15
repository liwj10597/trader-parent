package com.mfml.trader.server.core.chatgpt;

import com.mfml.trader.server.core.chatgpt.ro.AskRo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;
import java.util.HashMap;

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
    public static final String accessToken = "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";

    @Override
    public String ask(AskRo ro) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HashMap<String, Object> params = new HashMap<>();
        params.put("model", ro.getModel());
        params.put("prompt", ro.getPrompt());
        params.put("max_tokens", 50);
        params.put("temperature", 0);
        return restTemplate.postForObject(url, params, String.class);
    }
}
