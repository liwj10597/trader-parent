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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author caozhou
 * @date 2023-03-07 14:50
 */
@Slf4j
@Component
public class BaseBOLLStrategy implements BaseStrategy {
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
        AbstractIndicator.Result boll = this.boll.boll(stockCode, date, Period.day.code, Recovery.before.code, -3);
        List<String> ma20List = boll.getList(BOLL.ma20);
        List<String> lbList = boll.getList(BOLL.lb);

        AbstractIndicator.Result volume = this.vol.volume(stockCode, date, Period.day.code, Recovery.before.code, -3);
        List<String> clostList = volume.getList(VOL.close);
        List<String> percentList = volume.getList(VOL.percent);
        List<String> lowList = volume.getList(VOL.low);

        // boll中轨走平
        Double ma20 = Double.valueOf(ma20List.get(ma20List.size() - 1));
        Double ma20_1 = Double.valueOf(ma20List.get(ma20List.size() - 2));
        Double ma20_2 = Double.valueOf(ma20List.get(ma20List.size() - 3));
        Double ma20_diff_1 = BigDecimal.valueOf(Math.abs(ma20 - ma20_1)).divide(BigDecimal.valueOf(ma20), RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP).doubleValue();
        Double ma20_1_diff_2 = BigDecimal.valueOf(Math.abs(ma20_1 - ma20_2)).divide(BigDecimal.valueOf(ma20_1), RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP).doubleValue();
        if (ma20_diff_1 > 0.01 || ma20_1_diff_2 > 0.01) {
            log.info("buy date={}, ma20_diff_1={}, ma20_1_diff_2={}", date, ma20_diff_1, ma20_1_diff_2);
            return ;
        }

        // 收盘价乖离率 > 2%且收 2个点以上的中阳
        Double close = Double.valueOf(clostList.get(clostList.size() - 1));
        Double lb = Double.valueOf(lbList.get(lbList.size() - 1));
        BigDecimal subtract = BigDecimal.valueOf(close).subtract(BigDecimal.valueOf(lb));
        double rate = subtract.divide(BigDecimal.valueOf(lb), RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP).doubleValue();
        if (rate < 0.02) {
            log.info("buy date={}, rate={}", date, rate);
            return ;
        }
        Double percent = Double.valueOf(percentList.get(percentList.size() - 1));
        if (percent < 2) {
            log.info("buy date={}, percent={}", date, percent);
            return ;
        }

        // 前一日最低价位于boll下轨附近
        Double low_1 = Double.valueOf(lowList.get(lowList.size() - 2));
        Double lb_1 = Double.valueOf(lbList.get(lbList.size() - 2));
        BigDecimal subtract1 = BigDecimal.valueOf(low_1).subtract(BigDecimal.valueOf(lb_1));
        double rate_1 = subtract1.divide(BigDecimal.valueOf(lb_1), RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP).doubleValue();
        if (rate_1 > 0.01) {
            log.info("buy date={}, rate_1={}", date, rate_1);
            return ;
        }

        log.info("----date={}, stockCode={}", date, stockCode);
    }

    @Override
    public void sell() {

    }
}
