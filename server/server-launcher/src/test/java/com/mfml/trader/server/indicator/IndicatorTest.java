package com.mfml.trader.server.indicator;

import com.mfml.trader.server.BaseTest;
import com.mfml.trader.server.core.indicator.Indicator;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.annotation.Resource;


/**
 * @author caozhou
 * @date 2022-11-30 21:51
 */
@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IndicatorTest extends BaseTest {

    @Resource
    Indicator indicator;

    @Test
    public void testIndicator() {
        String result = indicator.indicators("600570", "SH", "2023-02-28", "day", "before", 2, Lists.newArrayList("macd", "ma"));
        System.out.println(result);
    }
}
