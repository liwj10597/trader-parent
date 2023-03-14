package com.mfml.trader.server.core.service.strategy.helper;

import com.mfml.trader.server.core.indicator.MA;
import com.mfml.trader.server.core.indicator.VOL;
import com.mfml.trader.server.core.indicator.base.AbstractIndicator;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author caozhou
 * @date 2023-03-13 10:45
 */
public class Helper {

    /**
     * 基础条件
     * @param ma 均线
     * @param vol 成交量、换手、开盘、收盘价等
     * @return
     */
    public static boolean basic(AbstractIndicator.Result ma, AbstractIndicator.Result vol) {
        List<String> ma5List = ma.getList(MA.ma5);
        Integer ma5Idx = ma5List.size() - 1;
        Double ma5_0 = Double.valueOf(ma5List.get(ma5Idx));
        Double ma5_1 = Double.valueOf(ma5List.get(ma5Idx - 1));

        List<String> ma10List = ma.getList(MA.ma10);
        Integer ma10Idx = ma10List.size() - 1;
        Double ma10_0 = Double.valueOf(ma10List.get(ma10Idx));
        Double ma10_1 = Double.valueOf(ma10List.get(ma10Idx - 1));


        // T日 ma5>ma10; T-1日 ma5<ma10
        Double ma_diff_0 = ma5_0 - ma10_0;
        Double ma_diff_1 = ma5_1 - ma10_1;
        if (ma_diff_0 < 0 || ma_diff_1 >= 0) {
            return false;
        }

        // T日 close>ma5
        List<String> closeList = vol.getList(VOL.close);
        Integer closeIdx = closeList.size() - 1;
        Double close_0 = Double.valueOf(closeList.get(closeIdx));
        if (close_0 < ma5_0) {
            return false;
        }
        // T-1日 close>ma5
        Double close_1 = Double.valueOf(closeList.get(closeIdx - 1));
        if (close_1 < ma5_1) {
            return false;
        }
        return true;
    }

    /**
     * 完美
     * @param ma
     * @param vol
     * @return
     */
    public static boolean perfect(AbstractIndicator.Result ma, AbstractIndicator.Result vol) {
        List<String> percentList = vol.getList(VOL.percent);
        Integer percentIdx = percentList.size() - 1;
        Double percent_0 = Double.valueOf(percentList.get(percentIdx));
        Double percent_1 = Double.valueOf(percentList.get(percentIdx - 1));

        List<String> openList = vol.getList(VOL.open);
        List<String> closeList = vol.getList(VOL.close);
        Integer priceIdx = openList.size() - 1;
        Double open_0 = Double.valueOf(openList.get(priceIdx));
        Double close_0 = Double.valueOf(closeList.get(priceIdx));
        Double open_1 = Double.valueOf(openList.get(priceIdx - 1));
        Double close_1 = Double.valueOf(closeList.get(priceIdx - 1));

        // T日为真阳线；T-1日为真阳线
        if (percent_0 > 0 && percent_1 > 0 && open_0 < close_0 && open_1 < close_1) {
            return true;
        }
        return false;
    }

    /**
     * 好
     */
    public static boolean well(AbstractIndicator.Result ma, AbstractIndicator.Result vol) {
        if (Helper.basic(ma, vol) && !Helper.perfect(ma, vol)) {
            return true;
        }
        return false;
    }

    /**
     * ok
     */
    public static boolean ok(AbstractIndicator.Result ma, AbstractIndicator.Result vol) {
        List<String> ma5List = ma.getList(MA.ma5);
        Integer ma5Idx = ma5List.size() - 1;
        Double ma5_0 = Double.valueOf(ma5List.get(ma5Idx));
        Double ma5_1 = Double.valueOf(ma5List.get(ma5Idx - 1));

        List<String> ma10List = ma.getList(MA.ma10);
        Integer ma10Idx = ma10List.size() - 1;
        Double ma10_0 = Double.valueOf(ma10List.get(ma10Idx));
        Double ma10_1 = Double.valueOf(ma10List.get(ma10Idx - 1));

        // T日 ma5>ma10; T-1日 ma5<ma10
        Double ma_diff_0 = ma5_0 - ma10_0;
        Double ma_diff_1 = ma5_1 - ma10_1;
        if (ma_diff_0 < 0 || ma_diff_1 >= 0) {
            return false;
        }

        // T日 close>ma5 || T-1日 close>ma5
        List<String> closeList = vol.getList(VOL.close);
        Integer closeIdx = closeList.size() - 1;
        Double close_0 = Double.valueOf(closeList.get(closeIdx));
        Double close_1 = Double.valueOf(closeList.get(closeIdx - 1));
        Integer count = 0;
        if (close_0 > ma5_0) {
            count++;
        }
        if (close_1 > ma5_1) {
            count++;
        }
        if (1 == count) {
            return true;
        } else {
            return false;
        }
    }

    public static Double bias(Double before, Double after) {
        return BigDecimal.valueOf(before).subtract(BigDecimal.valueOf(after)).divide(BigDecimal.valueOf(after), 3, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
