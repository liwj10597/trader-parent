package com.mfml.trader.server.core.service.chatgpt;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.mfml.trader.common.core.result.CodeUtil;
import com.mfml.trader.common.core.result.Result;
import com.mfml.trader.common.core.result.ResultUtil;
import com.mfml.trader.server.core.client.StreamClient;
import com.mfml.trader.server.core.service.chatgpt.ro.EmbeddingRo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author caozhou
 * @date 2023-05-04 15:14
 */
@Slf4j
@Service
public class EmbeddingFacadeImpl implements EmbeddingFacade {
    @Resource
    RestTemplate restTemplate;

    public static final String embeddingsUrl = "https://api.openai.com/v1/embeddings";

    @Override
    public Result<List<String>> embeddings(EmbeddingRo ro) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(StreamClient.accessTokens.get(RandomUtil.randomInt(StreamClient.accessTokens.size())));
            headers.setAcceptCharset(Lists.newArrayList(Charsets.UTF_8));

            HashMap<String, Object> params = new HashMap<>();
            params.put("input", ro.getInput());
            params.put("model", "text-embedding-ada-002");

            ResponseEntity<String> exchange = restTemplate.exchange(embeddingsUrl, HttpMethod.POST, new HttpEntity<>(params, headers), String.class);
            String strBody = exchange.getBody();
            log.info("{}", strBody);
            JSONObject body = JSON.parseObject(strBody);
            JSONArray embeddings = body.getJSONArray("data").getJSONObject(0).getJSONArray("embedding");
            List<String> data = Lists.newArrayList();
            for (Object obj : embeddings) {
                data.add(String.valueOf(obj));
            }
            return ResultUtil.success(data);
        } catch (Exception e) {
            return ResultUtil.fail(CodeUtil.FAILED, e.getMessage());
        }
    }
}
