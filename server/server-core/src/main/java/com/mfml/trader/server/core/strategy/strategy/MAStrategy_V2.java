package com.mfml.trader.server.core.strategy.strategy;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.mfml.trader.common.core.enums.Period;
import com.mfml.trader.common.core.enums.Recovery;
import com.mfml.trader.common.core.regression.Bias;
import com.mfml.trader.server.core.indicator.BOLL;
import com.mfml.trader.server.core.indicator.MA;
import com.mfml.trader.server.core.indicator.MACD;
import com.mfml.trader.server.core.indicator.VOL;
import com.mfml.trader.server.core.indicator.base.AbstractIndicator;
import com.mfml.trader.server.core.strategy.strategy.base.BaseStrategy;
import com.mfml.trader.server.core.strategy.strategy.helper.StrategyHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author caozhou
 * @date 2023-03-10 22:00
 */
@Slf4j
@Component
public class MAStrategy_V2 implements BaseStrategy {
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
        AbstractIndicator.Result vol = this.vol.volume(stockCode, date, Period.day.code, Recovery.before.code, -5);

        List<String> ma5List = ma.getList(MA.ma5);
        Integer ma5Idx = ma5List.size() - 1;
        Double ma5_0 = Double.valueOf(ma5List.get(ma5Idx));
        List<String> closeList = vol.getList(VOL.close);
        Integer closeIdx = closeList.size() - 1;
        Double close_0 = Double.valueOf(closeList.get(closeIdx));
        List<String> percentList = vol.getList(VOL.percent);
        Integer percentIdx = percentList.size() - 1;
        Double percent_0 = Double.valueOf(percentList.get(percentIdx));
        Double bias = Bias.bias(close_0, ma5_0);

        if (StrategyHelper.basic(ma, vol) && StrategyHelper.perfect(ma, vol) && bias <= 0.1) {
            System.out.println(String.join(",", date, stockCode, "perfect"));
            return;
        }

        String beforeDate = DateUtil.format(DateUtil.offsetDay(DateUtil.parse(date, DatePattern.NORM_DATE_PATTERN).toJdkDate(), -1), DatePattern.NORM_DATE_PATTERN);
        AbstractIndicator.Result ma_1 = this.ma.ma(stockCode, beforeDate, Period.day.code, Recovery.before.code, -5);
        AbstractIndicator.Result vol_1 = this.vol.volume(stockCode, beforeDate, Period.day.code, Recovery.before.code, -5);

        if (StrategyHelper.well(ma_1, vol_1) && close_0 > ma5_0 && bias <= 0.1) {
            System.out.println(String.join(",", date, stockCode, "well"));
            return;
        }

        if (StrategyHelper.ok(ma_1, vol_1) && close_0 > ma5_0 && percent_0 > 0 && bias <= 0.1) {
            System.out.println(String.join(",", date, stockCode, "ok"));;
            return;
        }
    }

    @Override
    public void sell() {

    }
}
