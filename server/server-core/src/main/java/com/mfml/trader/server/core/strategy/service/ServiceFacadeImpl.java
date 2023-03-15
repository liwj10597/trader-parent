package com.mfml.trader.server.core.strategy.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mfml.trader.common.core.enums.Period;
import com.mfml.trader.common.core.enums.Recovery;
import com.mfml.trader.server.core.indicator.VOL;
import com.mfml.trader.server.core.indicator.base.AbstractIndicator;
import com.mfml.trader.server.core.strategy.strategy.BOLLStrategy;
import com.mfml.trader.server.core.strategy.strategy.MAStrategy;
import com.mfml.trader.server.core.strategy.strategy.MAStrategy_V2;
import com.mfml.trader.server.dao.domain.FundsDo;
import com.mfml.trader.server.dao.domain.StocksDo;
import com.mfml.trader.server.dao.mapper.FundsMapper;
import com.mfml.trader.server.dao.mapper.StocksMapper;
import com.mfml.trader.server.dao.mapper.TradeRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @author caozhou
 * @date 2023-02-28 22:43
 */
@Slf4j
@Service
public class ServiceFacadeImpl implements ServiceFacade {
   @Resource
   MAStrategy maStrategy;
   @Resource
   BOLLStrategy bollStrategy;
   @Resource
   MAStrategy_V2 maStrategyV2;
   @Resource
   FundsMapper fundsMapper;
   @Resource
   StocksMapper stocksMapper;
   @Resource
   TradeRecordMapper tradeRecordMapper;
   @Resource
   VOL vol;

    @Override
    public void service(String date, String stockCode) {
        // 查持仓
        StocksDo stock = stocksMapper.selectOne(new LambdaQueryWrapper<StocksDo>().eq(StocksDo::getDate, date).eq(StocksDo::getStockCode, stockCode));
        stock = Optional.ofNullable(stock).orElse(new StocksDo());
        Integer stockAmount = Optional.ofNullable(stock.getStockAmount()).orElse(0);

        // 没有持仓
        if (stockAmount <= 0) {
           Boolean buyHit = maStrategyV2.buyHit(date, stockCode);
           // 查看是否符合买入条件
           if (buyHit) {
               FundsDo funds = Optional.ofNullable(fundsMapper.selectOne(new LambdaQueryWrapper<FundsDo>().eq(FundsDo::getDate, date))).orElse(new FundsDo());
               Double fundsAmount = Optional.ofNullable(funds.getFundsAmount()).orElse(0D);
               AbstractIndicator.Result vol = this.vol.volume(stockCode, date, Period.day.code, Recovery.before.code, -1);
               List<String> closeList = vol.getList(VOL.close);
               Double close = Double.valueOf(closeList.get(0));

               int hands = BigDecimal.valueOf(fundsAmount).divide(BigDecimal.valueOf(close * 100), 0, BigDecimal.ROUND_DOWN).intValue();
               // 买入操作

           }
        } else {
            // 有持仓
            Boolean sellHit = maStrategyV2.sellHit();
            // 查看是否符合卖出条件
            if (sellHit) {
                // 卖出操作
            }
        }


    }
}
