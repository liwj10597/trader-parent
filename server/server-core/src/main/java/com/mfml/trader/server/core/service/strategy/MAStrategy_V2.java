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
        Double ma5_1 = Double.valueOf(ma5List.get(ma5Idx - 1));

        List<String> ma10List = ma.getList(MA.ma10);
        Integer ma10Idx = ma10List.size() - 1;
        Double ma10_0 = Double.valueOf(ma10List.get(ma10Idx));
        Double ma10_1 = Double.valueOf(ma10List.get(ma10Idx - 1));


        // T日 ma5>ma10; T-1日 ma5<ma10
        Double ma_diff_0 = ma5_0 - ma10_0;
        Double ma_diff_1 = ma5_1 - ma10_1;
        if (ma_diff_0 < 0 || ma_diff_1 >= 0) {
            return ;
        }

        // T日 close>ma5
        List<String> closeList = vol.getList(VOL.close);
        Integer closeIdx = closeList.size() - 1;
        Double close_0 = Double.valueOf(closeList.get(closeIdx));
        if (close_0 < ma5_0) {
            return ;
        }
        // T-1日 close>ma5
        Double close_1 = Double.valueOf(closeList.get(closeIdx - 1));
        if (close_1 < ma5_1) {
            return ;
        }

        System.out.println(date + "," + stockCode);
    }

    @Override
    public void sell() {

    }
}
