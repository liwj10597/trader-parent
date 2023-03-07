package com.mfml.trader.common.core.enums;

import lombok.AllArgsConstructor;

/**
 * 1m  5m 15m 30m 60m 120m day week month quarter year
 */
@AllArgsConstructor
public enum Period {
    min_1("1m", "1分钟"),
    min_5("5m", "5分钟"),
    min_15("15m","15分钟"),
    min_30("30m","30分钟"),
    min_60("60m","60分钟"),
    min_120("120m","120分钟"),
    day("day","天"),
    week("week","周"),
    month("month","月"),
    quarter("quarter","季"),
    year("year","年");

    public  String code;

    public String desc;
}
