package com.mfml.trader.server.core.indicator;

import com.google.common.collect.Lists;
import com.mfml.trader.common.core.utils.JsonUtils;
import com.mfml.trader.server.core.indicator.base.AbstractIndicator;
import com.mfml.trader.server.core.indicator.execute.Indicator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author caozhou
 * @date 2023-02-28 18:42
 */
@Slf4j
@Component
public class KDJ extends AbstractIndicator {
    @Resource
    Indicator indicator;

    /**
     * 获取boll通道指标
     * kdjk：k 中线
     * kdjd：d 慢线
     * kdjj：j 快线
     *
     * @param stockCode 正股代码   600570
     * @param begin 日期   yyyy-MM-dd
     * @param period 指标周期 1m  5m 15m 30m 60m 120m day week month quarter year
     * @param type 复权类型  before 前复权； after 后复权；normal 不复权
     * @param count 周期个数 正数代表未来；负数代表过去；
     */
    public Result kdj(String stockCode, String begin, String period, String type, Integer count) {
        String json = indicator.indicators(stockCode, begin, period, type, count, Lists.newArrayList("kdj"));
        return JsonUtils.parseObject(json, Result.class);
    }
}
