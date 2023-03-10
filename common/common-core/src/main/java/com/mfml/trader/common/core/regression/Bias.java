package com.mfml.trader.common.core.regression;

import com.mfml.trader.common.core.exception.TraderException;

import java.math.BigDecimal;

/**
 * 乖离率
 * @author caozhou
 * @date 2023-03-08 15:27
 */
public class Bias {

    /**
     * 乖离率
     * @param before 除数
     * @param after 被除数
     * @return 乖离率
     */
    public static double bias(Double before, Double after) {
        if (0 == after || null == after) {
            throw new TraderException("被除数不能改为0");
        }
        BigDecimal subtract = BigDecimal.valueOf(before).subtract(BigDecimal.valueOf(after));
        return subtract.divide(BigDecimal.valueOf(after), 3, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
