package com.mfml.trader.server.strategy;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.common.collect.Lists;
import com.mfml.trader.server.BaseTest;
import com.mfml.trader.server.core.indicator.helper.IndicatorHelper;
import com.mfml.trader.server.core.strategy.manage.ManageFacade;
import com.mfml.trader.server.dao.domain.FundsDo;
import com.mfml.trader.server.dao.domain.StocksDo;
import com.mfml.trader.server.dao.domain.TradeRecordDo;
import com.mfml.trader.server.dao.mapper.FundsMapper;
import com.mfml.trader.server.dao.mapper.StocksMapper;
import com.mfml.trader.server.dao.mapper.TradeRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author caozhou
 * @date 2023-03-14 15:44
 */
@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StrategyTest extends BaseTest {

    @Resource
    FundsMapper fundsMapper;
    @Resource
    StocksMapper stocksMapper;
    @Resource
    TradeRecordMapper tradeRecordMapper;
    @Resource
    ManageFacade serviceFacade;
    @Resource
    IndicatorHelper indicatorHelper;

    List<String> tradeDays = Lists.newArrayList();

    /**
     * 前置
     */
    @Before
    public void before() {
        // 清理上次的结果
        stocksMapper.delete(new LambdaUpdateWrapper<StocksDo>().gt(StocksDo::getId, 0L));
        fundsMapper.delete(new LambdaUpdateWrapper<FundsDo>().gt(FundsDo::getId, 0L));
        tradeRecordMapper.delete(new LambdaUpdateWrapper<TradeRecordDo>().gt(TradeRecordDo::getId, 0L));

        // 初始化交易日历
        tradeDays.addAll(indicatorHelper.trade_cal("SSE", "2021-03-14", "2023-03-14"));

        // 初始化资金
        FundsDo funds = new FundsDo();
        funds.setDate(tradeDays.get(0));
        funds.setFundsAmount(100000D);
        fundsMapper.insert(funds);
    }

    /**
     * 回测
     */
    @Test
    public void strategyTest() {
        // 循环历史交易日
        for (int idx = 0; idx < tradeDays.size(); idx++) {
            // 买卖操作
            String date = tradeDays.get(idx);
            serviceFacade.manage(date);

            if (idx + 1 < tradeDays.size()) {
                // 清算
                liquidation(date, tradeDays.get(idx + 1));
            }
        }
    }

    /**
     * 清算
     * @param date
     * @param nextDate
     */
    public void liquidation(String date, String nextDate) {
        // 资金
        FundsDo funds = fundsMapper.selectOne(new LambdaQueryWrapper<FundsDo>().eq(FundsDo::getDate, date));
        funds = Optional.ofNullable(funds).orElse(new FundsDo());
        FundsDo entity = new FundsDo();
        entity.setDate(nextDate);
        entity.setFundsAmount(funds.getFundsAmount());
        fundsMapper.insert(entity);
        // 持仓
        List<StocksDo> stocks = stocksMapper.selectList(new LambdaQueryWrapper<StocksDo>().eq(StocksDo::getDate, date));
        for (StocksDo stock : stocks) {
            if (stock.getStockAmount()> 0) {
                StocksDo sdo = new StocksDo();
                sdo.setStockCode(stock.getStockCode());
                sdo.setStockAmount(stock.getStockAmount());
                sdo.setCostPrice(stock.getCostPrice());
                sdo.setDate(nextDate);
                sdo.setBuyDate(stock.getBuyDate());
                stocksMapper.insert(sdo);
            }
        }
    }
}
