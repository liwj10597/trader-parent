package com.mfml.trader.server.core.strategy.strategy.base;

/**
 * @author caozhou
 * @date 2023-03-07 14:53
 */
public interface BaseStrategy {
    /**
     * 买入提示
     * @param date 日期，主要用于回测
     * @param stockCode 正股代码
     */
    Boolean buyHit(String date, String stockCode);

    /**
     * 卖出提示
     */
    Boolean sellHit();
}
