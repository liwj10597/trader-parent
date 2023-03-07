package com.mfml.trader.server.core.indicator;

import com.google.common.collect.Lists;
import com.mfml.trader.common.core.utils.JsonUtils;
import com.mfml.trader.server.core.indicator.base.AbstractIndicator;
import com.mfml.trader.server.core.indicator.execute.Indicator;
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

    public static final String ma5 = "ma5";
    public static final String ma10 = "ma10";
    public static final String ma20 = "ma20";
    public static final String ma30 = "ma30";

    /**
     * 获取均线指标
     * timestamp: 时间戳 单位毫秒
     * ma5: 5周期均线
     * ma10: 10周期均线
     * ma20: 20周期均线
     * ma30 30周期均线
     *
     * @param stockCode 正股代码   600570
     * @param begin 日期   yyyy-MM-dd
     * @param period 指标周期 1m  5m 15m 30m 60m 120m day week month quarter year
     * @param type 复权类型  before 前复权； after 后复权；normal 不复权
     * @param count 周期个数 正数代表未来；负数代表过去；
     * @return
     */
    public Result ma(String stockCode, String begin, String period, String type, Integer count) {
        String json = indicator.indicators(stockCode, begin, period, type, count, Lists.newArrayList("ma"));
        return JsonUtils.parseObject(json, Result.class);
    }
}
