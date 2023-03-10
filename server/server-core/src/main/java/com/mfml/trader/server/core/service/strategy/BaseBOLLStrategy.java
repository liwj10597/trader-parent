package com.mfml.trader.server.core.service.strategy;

import com.mfml.trader.common.core.enums.Period;
import com.mfml.trader.common.core.enums.Recovery;
import com.mfml.trader.common.core.regression.Bias;
import com.mfml.trader.common.core.regression.Regression;
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
        int count = 4;
        AbstractIndicator.Result boll = this.boll.boll(stockCode, date, Period.day.code, Recovery.before.code, -count);
        List<String> ma20List = boll.getList(BOLL.ma20);
        List<String> lbList = boll.getList(BOLL.lb);

        AbstractIndicator.Result volume = this.vol.volume(stockCode, date, Period.day.code, Recovery.before.code, -count);
        List<String> clostList = volume.getList(VOL.close);
        List<String> openList = volume.getList(VOL.open);
        List<String> percentList = volume.getList(VOL.percent);
        List<String> lowList = volume.getList(VOL.low);

        // 1.近3天 boll中轨走平
        /*Double k = Regression.linearFit(Regression.linearScatters(ma20List));
        if (k < -1.0) {
            log.info("buy date={}, 近{}天boll中轨拟合斜率{}，不符买入条件", date, count, k);
            return ;
        }*/

        // 2.当日收盘价乖离率 > 0.02 (2%)，表示远离boll线下轨
        Double close = Double.valueOf(clostList.get(clostList.size() - 1));
        Double lb = Double.valueOf(lbList.get(lbList.size() - 1));
        double bias = Bias.bias(close, lb);
        if (bias < 0.02) {
            //log.info("buy date={}, 当日收盘价乖离率bias={}，不符买入条件", date, bias);
            return ;
        }

        // 3.当日涨了2个点以上
        Double percent = Double.valueOf(percentList.get(percentList.size() - 1));
        if (percent < 2) {
            //log.info("buy date={}, 涨跌幅percent={}，不符买入条件", date, percent);
            return ;
        }

        // 4.当日收真阳线
        Double open = Double.valueOf(openList.get(openList.size() - 1));
        Double p = close - open;
        if (p < 0) {
            //log.info("buy date={}, 当日收假阳线，不符买入条件");
            return ;
        }

        // 5.开盘价、收盘价均在boll下轨之上
        if (open < lb || close < lb) {
            //log.info("buy date={}, 开盘价或收盘价在boll下轨之下，不符买入条件");
            return;
        }

        // 前一日最低价位于boll下轨附近
        Double low_1 = Double.valueOf(lowList.get(lowList.size() - 2));
        Double lb_1 = Double.valueOf(lbList.get(lbList.size() - 2));
        double bias1 = Bias.bias(low_1, lb_1);
        if (bias1 > 0.01) {
            //log.info("buy date={}, 上交易日最低价与boll下轨乖离率bias1={}，不符买入条件", date, bias1);
            return ;
        }

        log.info("ok date={}, stockCode={}", date, stockCode);
    }

    @Override
    public void sell() {

    }
}
