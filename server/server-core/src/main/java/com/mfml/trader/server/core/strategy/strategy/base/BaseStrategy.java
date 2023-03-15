package com.mfml.trader.server.core.strategy.strategy.base;

/**
 * @author caozhou
 * @date 2023-03-07 14:53
 */
public interface BaseStrategy {
    /**
     * 买入提示
     * @param date 日期，yyyy-MM-dd
     * @param stockCode 正股代码
     */
    Boolean buyHit(String date, String stockCode);

    /**
     * 卖出提示
     * @param date 日期，yyyy-MM-dd
     * @param stockCode 股票代码
     * @param costPrice 成本价 单位 元
     */
    Boolean sellHit(String date, String stockCode, Double costPrice);
}
