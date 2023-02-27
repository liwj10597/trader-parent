package com.mfml.trader.common.core.utils;

import cn.hutool.core.date.DatePattern;

import java.util.Calendar;
import java.util.Date;

/**
 * @ProjectName: keygenerator
 * @Description: 日期处理工具

 */
public abstract class DateUtil {
    /**
     * 返回当前日期格式yyMdd, M是1-9月，10月（0），11月（N）, 12月（D）
     * @return 日期字符串
     */
    public static String getNowShortDateFormatStr() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        String monthStr = null;
        if (month < 10) {
            monthStr = String.valueOf(month);
        } else if (month == 10) {
            monthStr = "0";
        } else if (month == 11) {
            monthStr = "N";
        } else if (month == 12) {
            monthStr = "D";
        }
        return String.format("%02d%s%02d", year % 100, monthStr, day);
    }

    public static String parse(long time) {
        return cn.hutool.core.date.DateUtil.format(new Date(time), DatePattern.PURE_DATETIME_PATTERN);
    }



}
