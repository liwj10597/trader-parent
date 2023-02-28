package com.mfml.trader.server.core.indicator;


import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * @author caozhou
 * @date 2023-02-28 15:20
 */
@Slf4j
@Component
public class Macd {
    @Resource
    Indicator indicator;

    public String macd(String stockCode, String market, String begin, String period, String type, Integer count) {

        return indicator.indicators(stockCode, market, begin, period, type, count, Lists.newArrayList("macd"));
    }



}
