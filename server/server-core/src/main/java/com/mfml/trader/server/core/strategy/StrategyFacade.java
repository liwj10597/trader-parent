package com.mfml.trader.server.core.strategy;

/**
 * 策略管理
 * @author caozhou
 * @date 2023-02-28 22:43
 */
public interface StrategyFacade {
    /**
     * 买入
     */
    void buy();

    /**
     * 卖出
     */
    void sell();
}
