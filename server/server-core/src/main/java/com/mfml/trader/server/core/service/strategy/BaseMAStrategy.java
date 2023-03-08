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
import java.util.List;

/**
 * @author caozhou
 * @date 2023-03-07 14:48
 */
@Slf4j
@Component
public class BaseMAStrategy implements BaseStrategy {
    @Resource
    private MACD macd;
    @Resource
    private MA ma;
    @Resource
    private VOL vol;
    @Resource
    private BOLL boll;

    public void buy(String date, String stockCode, Integer amount) {
        log.info("buy date={}", date);
        AbstractIndicator.Result ma = this.ma.ma(stockCode, date, Period.day.code, Recovery.before.code, -5);
        AbstractIndicator.Result vol = this.vol.volume(stockCode, date, Period.day.code, Recovery.before.code, -5);

        List<String> ma5List = ma.getList(MA.ma5);
        Double ma5 = Double.valueOf(ma5List.get(ma5List.size() - 1));
        Double ma5_1 = Double.valueOf(ma5List.get(ma5List.size() - 2));
        Double ma5_2 = Double.valueOf(ma5List.get(ma5List.size() - 3));

        List<String> ma10List = ma.getList(MA.ma10);
        Double ma10 = Double.valueOf(ma10List.get(ma10List.size() - 1));
        Double ma10_1 = Double.valueOf(ma10List.get(ma10List.size() - 2));


        // 当日五日线 > 十日线  昨日 五日线 < 十日线
        Double ma5_dif_ma10 = ma5 - ma10;
        Double ma5_1_dif_ma10_1 = ma5_1 - ma10_1;
        if (ma5_dif_ma10 <= 0 || ma5_1_dif_ma10_1 >= 0) {
            return ;
        }

        // 近3日五日线多头排列
        Double ma5_dif_1 = ma5 - ma5_1;
        Double ma5_dif_2 = ma5_1 - ma5_2;
        if (ma5_dif_1 < 0 || ma5_dif_2 < 0) {
            return ;
        }

        // 近2日十日线多头排列
        Double ma10_dif_1 = ma10 - ma10_1;
        if (ma10_dif_1 < 0) {
            return ;
        }

        // 当日收涨
        List<String> klineList = vol.getList(VOL.percent);
        String percent = klineList.get(klineList.size() - 1);
        Double percentD = Double.valueOf(percent);
        if (percentD < 0) {
            return ;
        }

        // 买入当日乖离率不能过高
        log.info("date={}, stockCode={}", date, stockCode);
    }

    @Override
    public void sell() {

    }
}
