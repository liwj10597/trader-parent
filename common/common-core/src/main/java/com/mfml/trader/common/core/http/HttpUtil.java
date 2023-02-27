package com.mfml.trader.common.core.http;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.mfml.trader.common.core.exception.HttpException;
import com.mfml.trader.common.core.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@Slf4j
@Component
public class HttpUtil {

    @Resource
    RestTemplate restTemplate;
    @Resource
    RestTemplate restTemplateCallback;

    /**
     * 封装模板get方法
     *
     * @param url 请求地址
     * @param paramMap 请求参数
     * @param responseClazz 返回对象的clzz对象
     */
    public <T> T get(String url, Map<String, Object> paramMap, Class<T> responseClazz) {
        try {
            ResponseEntity<T> entity = restTemplate.getForEntity(url, responseClazz, paramMap);
            if (!entity.getStatusCode().is2xxSuccessful()) {
                throw new HttpException(String.join("|").join("http status exection","req" , JsonUtils.toJSONString(paramMap), "res", JsonUtils.toJSONString(entity)));
            }
            return entity.getBody();
        } catch (Exception e) {
            throw new HttpException("post exception " + JsonUtils.toJSONString(paramMap), e);
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

    /**
     * 回调方法
     *
     * @param url 请求地址
     * @param paramMap 请求参数
     * @param responseClazz 返回对象的clzz对象
     */
    public <T> T callback(String url, MultiValueMap<String, Object> paramMap, Class<T> responseClazz) {
        try {
            // 手动导入的话费单没有回调地址,无需回调
            if (StringUtils.isBlank(url)) {
               log.info("callback url is null paramMap={}", paramMap);
               return responseClazz.newInstance();
            }
            RequestEntity requestEntity = RequestEntity.post(null)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .body(paramMap);
            ResponseEntity<T> entity = restTemplateCallback.postForEntity(url, requestEntity, responseClazz);
            if (!entity.getStatusCode().is2xxSuccessful()) {
                throw new HttpException(String.join("|").join("http status exection","req" , JsonUtils.toJSONString(paramMap), "res", JsonUtils.toJSONString(entity)));
            }
            return entity.getBody();
        } catch (Exception e) {
            throw new HttpException("callback exception " + JsonUtils.toJSONString(paramMap), e);
        }
    }
}
