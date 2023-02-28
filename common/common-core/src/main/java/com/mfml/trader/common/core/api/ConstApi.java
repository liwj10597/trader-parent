package com.mfml.trader.common.core.api;

/**
 * @author caozhou
 * @date 2023-02-28 15:00
 */
public class ConstApi {
    /**
     * 雪球
     * 获取历史行情接口
     */
    public static String xueQiuApi = "https://stock.xueqiu.com/v5/stock/chart/kline.json?symbol=[symbol]&begin=[begin]&period=[period]&type=[type]&count=[count]&indicator=[indicator]";
    /**
     * 东方财富
     * 获取历史开盘价、收盘价等历史数据
     */
    public static String dongCaiApi = "https://push2his.eastmoney.com/api/qt/stock/kline/get";

    /**
     * 量化股票接口
     * https://stockapi.com.cn/#/nineTrun
     */
    public static String stockApi = "https://stockapi.com.cn/v1/quota/nineTurn?code=[stockCode]&date=[date]";
}
