package com.mfml.trader.server.indicator;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.mfml.trader.server.BaseTest;
import com.mfml.trader.server.core.indicator.base.AbstractIndicator;
import com.mfml.trader.server.core.indicator.BOLL;
import com.mfml.trader.server.core.indicator.execute.Indicator;
import com.mfml.trader.server.core.indicator.MACD;
import com.mfml.trader.server.core.strategy.StrategyFacade;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import javax.annotation.Resource;
import java.util.Date;


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
    StrategyFacade strategyFacade;

    @Test
    public void testStrategy() {
        for (int i = 730; i > 0; i--) {
            try {
                Date date = DateUtil.offsetDay(new Date(), -i).toJdkDate();
                String format = DateUtil.format(date, DatePattern.NORM_DATE_PATTERN);
                strategyFacade.buy(format, "600570", 200);
                Thread.sleep(200);
            } catch (Exception e) {
                log.warn("", e);
            }
        }
    }

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
