package com.mfml.trader.server.core.strategy.manage;

/**
 * 策略管理
 * @author caozhou
 * @date 2023-02-28 22:43
 */
public interface ManageFacade {
    /**
     * 策略管理
     * @param date yyyy-MM-dd
     */
    void manage(String date);

    /**
     * 股票买入
     * @param date yyyy-MM-dd
     * @param stockCode 股票代码
     * @param amount 买入数量，单位股
     * @param price 买入价格，单位元
     */
    void buy(String date, String stockCode, Integer amount, Double price);

    /**
     * 股票卖出
     * @param id 该笔持仓的编码
     * @param date yyyy-MM-dd
     * @param stockCode 股票代码
     * @param amount 卖出数量，单位股
     * @param price 卖出价格，单位元
     */
    void sell(Long id, String date, String stockCode, Integer amount, Double price);
}
