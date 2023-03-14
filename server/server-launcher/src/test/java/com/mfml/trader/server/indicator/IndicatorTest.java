package com.mfml.trader.server.indicator;

import com.mfml.trader.server.BaseTest;
import com.mfml.trader.server.core.indicator.base.AbstractIndicator;
import com.mfml.trader.server.core.indicator.BOLL;
import com.mfml.trader.server.core.indicator.execute.Indicator;
import com.mfml.trader.server.core.indicator.MACD;
import com.mfml.trader.server.core.indicator.helper.IndicatorHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import javax.annotation.Resource;
import java.util.List;


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
    @Resource
    IndicatorHelper indicatorHelper;


    /**
     * 测试获取交易日
     */
    @Test
    public void testTradeCal() {
        List<String> s = indicatorHelper.trade_cal("SSE", "20210202", "20230314");
        System.out.println(s);
    }

    /**
     * 测试指标
     */
    @Test
    public void testIndicator() {
        //String result = indicator.indicators("600570", "2023-02-28", "day", "before", -2, Lists.newArrayList("macd"));
        //String result = indicator.indicators("600570", "2023-02-28", "day", "before", -2, Lists.newArrayList( "ma"));
        //System.out.println(result);
    }

    /**
     * 测试MACD
     */
    @Test
    public void testMACD() {
        AbstractIndicator.Result macd = this.macd.macd("600570",  "2023-02-28", "day", "before", -2);
        System.out.println(macd);
    }

    /**
     * 测试boll
     */
    @Test
    public void testBOLL() {
        AbstractIndicator.Result boll = this.boll.boll("600570",  "2023-02-28", "day", "before", -2);
        System.out.println(boll);
    }


}
