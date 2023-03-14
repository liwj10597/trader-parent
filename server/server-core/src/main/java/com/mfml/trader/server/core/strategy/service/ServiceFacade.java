package com.mfml.trader.server.core.strategy.service;

/**
 * 策略管理
 * @author caozhou
 * @date 2023-02-28 22:43
 */
public interface ServiceFacade {
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
