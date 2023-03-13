package com.mfml.trader.server.core.service.strategy;

import com.mfml.trader.common.core.enums.Period;
import com.mfml.trader.common.core.enums.Recovery;
import com.mfml.trader.server.core.indicator.BOLL;
import com.mfml.trader.server.core.indicator.MA;
import com.mfml.trader.server.core.indicator.MACD;
import com.mfml.trader.server.core.indicator.VOL;
import com.mfml.trader.server.core.indicator.base.AbstractIndicator;
import com.mfml.trader.server.core.service.strategy.base.BaseStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author caozhou
 * @date 2023-03-10 22:00
 */
@Slf4j
@Component
public class MABollStrategy implements BaseStrategy {
    @Resource
    private MACD macd;
    @Resource
    private MA ma;
    @Resource
    private VOL vol;
    @Resource
    private BOLL boll;
    @Override
    public void buy(String date, String stockCode, Integer amount) {
        AbstractIndicator.Result ma = this.ma.ma(stockCode, date, Period.day.code, Recovery.before.code, -5);
    }

    @Override
    public void sell() {

    }
}
