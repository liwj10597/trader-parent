package com.mfml.trader.server.core.indicator.base;

import com.mfml.trader.common.core.model.base.ToString;
import lombok.Data;

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
    }
}
