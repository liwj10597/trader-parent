package com.mfml.trader.common.core.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;

/**
 * @author caozhou
 * @date 2023-03-01 17:08
 */
public class DateUtils extends DateUtil {

    /**
     * 偏移天
     *
     * @param date   日期,格式必须是yyyy-MM-dd
     * @param offset 偏移天数，正数向未来偏移，负数向历史偏移
     * @return 偏移后的日期
     */
    public static String offsetDay(String date, int offset) {
        return DateUtil.format(DateUtil.offsetDay(DateUtil.parse(date, DatePattern.NORM_DATE_PATTERN), offset).toJdkDate(), DatePattern.NORM_DATE_PATTERN);
    }
}
