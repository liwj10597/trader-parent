package com.mfml.trader.server.core.indicator.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mfml.trader.common.core.api.ConstApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author caozhou
 * @date 2023-03-14 09:24
 */
@Slf4j
@Component
public class IndicatorHelper {
    @Resource
    RestTemplate restTemplate;

    @Value("${tushare.token}")
    String tushareToken;

    /**
     * 获取交易日历列表
     *
     * 来源 https://tushare.pro/document/1?doc_id=290
     * 频次 50次/分
     *
     * @param exchange 交易市场  SSE上交所,SZSE深交所,CFFEX 中金所,SHFE 上期所,CZCE 郑商所,DCE 大商所,INE 上能源
     * @param start 开始日期 yyyyMMdd
     * @param end 结束日期 yyyyMMdd
     * @return
     */
    public List<String> trade_cal(String exchange, String start, String end) {
        Map<String, Object> body = Maps.newHashMap();
        body.put("api_name", "trade_cal");
        body.put("token", tushareToken);
        Map<String, Object> params = Maps.newHashMap();
        params.put("exchange", exchange);
        params.put("start_date", start);
        params.put("end_date", end);
        params.put("is_open", "1");
        body.put("params", params);
        body.put("fields", "cal_date");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAcceptCharset(Lists.newArrayList(Charsets.UTF_8));
        String json = restTemplate.postForObject(ConstApi.tushareApi, new HttpEntity(body, headers), String.class);

        JSONArray jsonArray = JSON.parseObject(json).getJSONObject("data").getJSONArray("items");
        List<String> rest = Lists.newArrayList();
        for (int idx = 0; idx < jsonArray.size(); idx++) {
            JSONArray array = jsonArray.getJSONArray(idx);
            rest.add(array.getString(0));
        }
        // 从过去到未来排序
        Collections.reverse(rest);
        return rest;
    }
}
