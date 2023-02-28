package com.mfml.trader.common.core.http;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.mfml.trader.common.core.exception.HttpException;
import com.mfml.trader.common.core.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;
import java.util.Map;


@Slf4j
@Component
public class HttpUtil {

    @Resource
    RestTemplate restTemplate;

    /**
     * 封装模板get方法
     *
     * @param url 请求地址
     * @param param 请求参数
     * @param responseClazz 返回对象的clzz对象
     */
    public <T> T get(String url, Map<String, Object> param, Class<T> responseClazz) {
        try {
            ResponseEntity<T> entity = restTemplate.getForEntity(url, responseClazz, param);
            if (!entity.getStatusCode().is2xxSuccessful()) {
                throw new HttpException(String.join("|").join("http status exection","req" , JsonUtils.toJSONString(param), "res", JsonUtils.toJSONString(entity)));
            }
            return entity.getBody();
        } catch (Exception e) {
            throw new HttpException("post exception " + JsonUtils.toJSONString(param), e);
        }
    }

    /**
     * 封装模板post方法
     *
     * @param url 请求地址
     * @param body 请求参数 当contentType为form表单时，传递MultiValueMap对象；当contentType为json时，传递bean对象
     * @param responseClazz 返回对象的clzz对象
     * @param contentType 内容类型  是form还是json等
     */
    public <T> T post(String url, Object body, Class<T> responseClazz, MediaType contentType) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(contentType);
            headers.setAcceptCharset(Lists.newArrayList(Charsets.UTF_8));

            ResponseEntity<T> entity = restTemplate.postForEntity(url, new HttpEntity(body, headers), responseClazz);
            if (!entity.getStatusCode().is2xxSuccessful()) {
                throw new HttpException(String.join("|").join("http status exception","body" , JsonUtils.toJSONString(body), "entity", JsonUtils.toJSONString(entity)));
            }
            return entity.getBody();
        } catch (Exception e) {
            throw new HttpException("post exception " + JsonUtils.toJSONString(body), e);
        }
    }
}
