package com.mfml.trader.server.core.indicator.base;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.mfml.trader.common.core.exception.TraderException;
import com.mfml.trader.common.core.model.base.ToString;
import lombok.Data;
import java.util.List;
import java.util.Objects;

/**
 * @author caozhou
 * @date 2023-02-28 17:42
 */
public abstract class AbstractIndicator {

    @Data
    public static class Result extends ToString {
        private String symbol;

        private String[] column;

        private String[][] item;

        public String get(String date, String indicator) {
            Integer pos = null;
            for (int i = 0; i < column.length; i++) {
                if (column[i].equals(indicator)) {
                    pos = i;
                    break;
                }
            }
            Objects.requireNonNull(pos, "指标" + indicator + "非法");

            for (int i = 0; i < item.length; i++) {
                if (item[i][0].equals(String.valueOf(DateUtil.parse(date, DatePattern.NORM_DATE_PATTERN).getTime()))) {
                    return item[i][pos];
                }
            }
            throw new TraderException("无法获取 " + date + "的" + indicator + "指标值");
        }

        public List<String> getList(String indicator) {
            Integer pos = null;
            for (int i = 0; i < column.length; i++) {
                if (column[i].equals(indicator)) {
                    pos = i;
                    break;
                }
            }
            Objects.requireNonNull(pos, "指标" + indicator + "非法");

            List<String> data = Lists.newLinkedList();
            for (int i = 0; i < item.length; i++) {
                data.add(item[i][pos]);
            }
            return data;
        }
    }
}
