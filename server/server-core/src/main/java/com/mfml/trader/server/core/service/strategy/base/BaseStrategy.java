package com.mfml.trader.server.core.service.strategy.base;

/**
 * @author caozhou
 * @date 2023-03-07 14:53
 */
public interface BaseStrategy {
    /**
     * 买入
     * @param date 日期，主要用于回测
     * @param stockCode 正股代码
     * @param amount 买入手数
     */
    void buy(String date, String stockCode, Integer amount);

    /**
     * 卖出
     */
    void sell();
}
