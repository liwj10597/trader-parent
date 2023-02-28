package com.mfml.trader.server.indicator;

import com.alibaba.fastjson.JSON;
import com.mfml.trader.server.BaseTest;
import com.mfml.trader.server.core.indicator.base.AbstractIndicator;
import com.mfml.trader.server.core.indicator.BOLL;
import com.mfml.trader.server.core.indicator.execute.Indicator;
import com.mfml.trader.server.core.indicator.MACD;
import lombok.extern.slf4j.Slf4j;
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
    @Resource
    MACD macd;
    @Resource
    BOLL boll;

    @Test
    public void testIndicator() {
        //String result = indicator.indicators("600570", "2023-02-28", "day", "before", -2, Lists.newArrayList("macd"));
        //String result = indicator.indicators("600570", "2023-02-28", "day", "before", -2, Lists.newArrayList( "ma"));
        //System.out.println(result);
    }

    @Test
    public void testMACD() {
        AbstractIndicator.Result macd = this.macd.macd("600570",  "2023-02-28", "day", "before", -2);
        System.out.println(macd);
    }

    @Test
    public void testBOLL() {
        AbstractIndicator.Result boll = this.boll.boll("600570",  "2023-02-28", "day", "before", -2);
        System.out.println(boll);
    }


}
