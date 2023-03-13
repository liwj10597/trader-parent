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
public class BOLLStrategy implements BaseStrategy {
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

        AbstractIndicator.Result ma = this.ma.ma(stockCode, date, Period.day.code, Recovery.before.code, -count);
        List<String> ma5List = ma.getList(MA.ma5);
        // 0.近3天 boll中轨走平
        /*Double k = Regression.linearFit(Regression.linearScatters(ma20List));
        if (k < -1.0) {
            log.info("buy date={}, 近{}天boll中轨拟合斜率{}，不符买入条件", date, count, k);
            return ;
        }*/

        // 1.当日收盘站上5日均线
        Double c = Double.valueOf(clostList.get(clostList.size() - 1));
        Double ma5 = Double.valueOf(ma5List.get(ma5List.size() - 1));
        if (c <= ma5) {
            //log.info("buy date={}, 突破后股价未站上5日均线，不符买入条件", date);
            return ;
        }

        // 2.T-1日收盘价乖离率 > 0.02 (2%)，表示远离boll线下轨
        Double close = Double.valueOf(clostList.get(clostList.size() - 2));
        Double lb = Double.valueOf(lbList.get(lbList.size() - 2));
        double bias = Bias.bias(close, lb);
        if (bias < 0.02) {
            //log.info("buy date={}, 当日收盘价乖离率bias={}，不符买入条件", date, bias);
            return ;
        }

        // 3.T-1日涨了2个点以上
        Double percent = Double.valueOf(percentList.get(percentList.size() - 2));
        if (percent < 2) {
            //log.info("buy date={}, 涨跌幅percent={}，不符买入条件", date, percent);
            return ;
        }

        // 4.T-1日收真阳线
        Double open = Double.valueOf(openList.get(openList.size() - 2));
        Double p = close - open;
        if (p < 0) {
            //log.info("buy date={}, 当日收假阳线，不符买入条件");
            return ;
        }

        // 5.T-1日开盘价、收盘价均在boll下轨之上
        if (open < lb || close < lb) {
            //log.info("buy date={}, 开盘价或收盘价在boll下轨之下，不符买入条件");
            return;
        }

        // T-2日最低价位于boll下轨附近
        Double low_1 = Double.valueOf(lowList.get(lowList.size() - 3));
        Double lb_1 = Double.valueOf(lbList.get(lbList.size() - 3));
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
