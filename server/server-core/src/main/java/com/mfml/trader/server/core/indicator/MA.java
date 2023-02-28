package com.mfml.trader.server.core.indicator;

import com.google.common.collect.Lists;
import com.mfml.trader.common.core.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 均线数据
 * @author caozhou
 * @date 2023-02-28 17:30
 */
@Slf4j
@Component
public class MA extends AbstractIndicator {
    @Resource
    Indicator indicator;

    /**
     *
     */
    public Result ma(String stockCode, String market, String begin, String period, String type, Integer count) {
        String json = indicator.indicators(stockCode, market, begin, period, type, count, Lists.newArrayList("ma"));
        return JsonUtils.parseObject(json, Result.class);
    }
}
