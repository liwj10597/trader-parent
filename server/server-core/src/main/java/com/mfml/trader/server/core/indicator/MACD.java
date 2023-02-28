package com.mfml.trader.server.core.indicator;


import com.google.common.collect.Lists;
import com.mfml.trader.common.core.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * macd数据
 * @author caozhou
 * @date 2023-02-28 15:20
 */
@Slf4j
@Component
public class MACD extends AbstractIndicator {
    @Resource
    Indicator indicator;


    /**
     * macd指标获取接口
     * @param stockCode 正股代码   600570
     * @param market 市场    HS 上海； SZ 深圳
     * @param begin 日期   yyyy-MM-dd
     * @param period 指标周期 1m  5m 15m 30m 60m 120m day week month quarter year
     * @param type 复权类型  before 前复权； after 后复权；normal 不复权
     * @param count 周期个数 正数代表未来；负数代表过去；
     * @return
     */
    public Result macd(String stockCode, String market, String begin, String period, String type, Integer count) {
        String json = indicator.indicators(stockCode, market, begin, period, type, count, Lists.newArrayList("macd"));
        return JsonUtils.parseObject(json, Result.class);
    }


}
