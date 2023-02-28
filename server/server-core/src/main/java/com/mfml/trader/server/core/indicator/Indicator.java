package com.mfml.trader.server.core.indicator;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.mfml.trader.common.core.api.ConstApi;
import com.mfml.trader.common.core.exception.HttpException;
import com.mfml.trader.common.core.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author caozhou
 * @date 2023-02-28 15:48
 */
@Slf4j
@Component
public class Indicator {
    @Resource
    RestTemplate restTemplate;

    @Value("${xueqiu.cookie:f506605271b170487ebdc7ad5a647bca282e09b6}")
    String xueQiuCookie;

    /**
     * 指标获取接口
     * @param stockCode 正股代码   600570
     * @param market 市场    HS 上海； SZ 深圳
     * @param begin 日期   yyyy-MM-dd
     * @param period 指标周期 1m  5m 15m 30m 60m 120m day week month quarter year
     * @param type 复权类型  before 前复权； after 后复权；normal 不复权
     * @param count 周期个数 正数代表未来；负数代表过去；
     * @param indicators 指标列表
     * @return
     */
    public String indicators(String stockCode, String market, String begin, String period, String type, Integer count, List<String> indicators) {
        String url = ConstApi.xueQiuApi.replace("[symbol]", market + stockCode)
                .replace("[begin]", String.valueOf(DateUtil.parse(begin, DatePattern.NORM_DATE_PATTERN).toJdkDate().getTime()))
                .replace("[period]", period)
                .replace("[type]", type)
                .replace("[count]", String.valueOf(count))
                .replace("[indicator]", convIndicator(indicators));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<String> cookies = Lists.newArrayList();
        cookies.add("xq_a_token=" + xueQiuCookie);
        headers.put(HttpHeaders.COOKIE, cookies);

        ResponseEntity<String> entity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity(headers), String.class);
        if (!entity.getStatusCode().is2xxSuccessful()) {
            throw new HttpException(String.join("|").join("http status exception", "entity", JsonUtils.toJSONString(entity)));
        }
        return entity.getBody();
    }

    String convIndicator(List<String> indicators) {
        indicators = Optional.ofNullable(indicators).orElse(Lists.newArrayList());
        StringBuilder builder = new StringBuilder();
        for (String indicator : indicators) {
            builder.append(indicator).append(",");
        }
        String r = builder.toString();
        return r.substring(0, r.length() - 1);
    }
}
