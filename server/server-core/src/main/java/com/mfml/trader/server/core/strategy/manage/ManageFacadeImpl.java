package com.mfml.trader.server.core.strategy.manage;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mfml.trader.common.core.enums.Period;
import com.mfml.trader.common.core.enums.Recovery;
import com.mfml.trader.common.core.exception.TraderException;
import com.mfml.trader.server.core.indicator.VOL;
import com.mfml.trader.server.core.indicator.base.AbstractIndicator;
import com.mfml.trader.server.core.indicator.helper.StockCodeHelper;
import com.mfml.trader.server.core.strategy.strategy.BOLLStrategy;
import com.mfml.trader.server.core.strategy.strategy.MAStrategy;
import com.mfml.trader.server.core.strategy.strategy.MAStrategy_V2;
import com.mfml.trader.server.dao.domain.FundsDo;
import com.mfml.trader.server.dao.domain.StocksDo;
import com.mfml.trader.server.dao.domain.TradeRecordDo;
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
public class ManageFacadeImpl implements ManageFacade {
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

    /**
     * 策略管理
     * @param date yyyy-MM-dd
     */
    @Override
    public void manage(String date) {
        // 查持仓
        List<StocksDo> stocks = stocksMapper.selectList(new LambdaQueryWrapper<StocksDo>().eq(StocksDo::getDate, date));
        for (StocksDo sdo : stocks) {
            String stockCode = sdo.getStockCode();
            Integer stockAmount = sdo.getStockAmount();
            Long id = sdo.getId();
            String buyDate = sdo.getBuyDate();
            Double costPrice = sdo.getCostPrice();

            if (stockAmount <= 0) {
                continue;
            }

            Boolean sellHit = maStrategyV2.sellHit(date, stockCode, costPrice);
            if (sellHit && date.compareTo(buyDate) > 0) {
                // 卖出
                AbstractIndicator.Result volume = this.vol.volume(stockCode, date, Period.day.code, Recovery.before.code, -1);
                Double price = Double.valueOf(volume.getList(VOL.close).get(0));
                sell(id, date, stockCode, stockAmount, price);
            }
        }

        // 遍历股票
        List<String> stockCodes = StockCodeHelper.stockCode;
        for (int idx = 0; idx < stockCodes.size(); idx++) {
            int randomInt = RandomUtil.randomInt(stockCodes.size());
            String stockCode = stockCodes.get(randomInt);
            Boolean buyHit = maStrategyV2.buyHit(date, stockCode);
            if (buyHit) {
                // 买入
                FundsDo fundsDo = fundsMapper.selectOne(new LambdaQueryWrapper<FundsDo>().eq(FundsDo::getDate, date));
                Double fundsAmount = fundsDo != null ? fundsDo.getFundsAmount() : 0D;

                AbstractIndicator.Result volume = this.vol.volume(stockCode, date, Period.day.code, Recovery.before.code, -1);
                Double price = Double.valueOf(volume.getList(VOL.close).get(0));

                int hands = BigDecimal.valueOf(fundsAmount).divide(BigDecimal.valueOf(price * 100),0, BigDecimal.ROUND_DOWN).intValue();
                if (hands > 0)
                    buy(date, stockCode, hands * 100, price);
            }
        }
    }

    @Override
    public void sell(Long id, String date, String stockCode, Integer amount, Double price) {

        // 扣减持仓
        StocksDo stocksDo = stocksMapper.selectOne(new LambdaQueryWrapper<StocksDo>().eq(StocksDo::getId, id));
        if (null == stocksDo || stocksDo.getStockAmount() <= 0 || stocksDo.getStockAmount() < amount) {
            throw new TraderException("持仓为零或不够扣减" + String.join(",", date, stockCode, String.valueOf(stocksDo.getStockAmount()), String.valueOf(stocksDo.getStockAmount())));
        }
        StocksDo stocksEntity = new StocksDo();
        stocksEntity.setId(stocksDo.getId());
        stocksEntity.setStockAmount(stocksDo.getStockAmount() - amount);
        stocksMapper.updateById(stocksEntity);

        // 增加资金
        FundsDo fundsDo = fundsMapper.selectOne(new LambdaQueryWrapper<FundsDo>().eq(FundsDo::getDate, date));
        Double fundsAmount = fundsDo != null ? fundsDo.getFundsAmount() : 0D;
        fundsAmount = BigDecimal.valueOf(fundsAmount).add(BigDecimal.valueOf(amount * price)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        FundsDo fundsEntity = new FundsDo();
        fundsEntity.setDate(date);
        fundsEntity.setFundsAmount(fundsAmount);
        if (null == fundsDo) {
            fundsMapper.insert(fundsEntity);
        } else {
            fundsEntity.setId(fundsDo.getId());
            fundsMapper.updateById(fundsEntity);
        }

        // 插入交易流水
        TradeRecordDo entity = new TradeRecordDo();
        entity.setDate(date);
        entity.setStockCode(stockCode);
        entity.setAmount(amount);
        entity.setDirection(2);
        entity.setPrice(price);
        tradeRecordMapper.insert(entity);

    }

    @Override
    public void buy(String date, String stockCode, Integer amount, Double price) {
        // 扣减资金
        FundsDo fundsDo = fundsMapper.selectOne(new LambdaQueryWrapper<FundsDo>().eq(FundsDo::getDate, date));
        Double fundsAmount = fundsDo != null ? fundsDo.getFundsAmount() : 0D;
        Double buyAmount = amount * price;
        if (null == fundsDo || fundsAmount < buyAmount) {
            throw new TraderException("资金不足不够扣减" + String.join(",", date, stockCode, String.valueOf(fundsAmount), String.valueOf(buyAmount)));
        }
        FundsDo fundsEntity = new FundsDo();
        fundsEntity.setId(fundsDo.getId());
        double rest = BigDecimal.valueOf(fundsAmount).subtract(BigDecimal.valueOf(buyAmount)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        fundsEntity.setFundsAmount(rest);
        fundsMapper.updateById(fundsEntity);

        // 增加持仓
        StocksDo stocksEntity = new StocksDo();
        stocksEntity.setStockCode(stockCode);
        stocksEntity.setDate(date);
        stocksEntity.setBuyDate(date);
        stocksEntity.setStockAmount(amount);
        stocksEntity.setCostPrice(price);
        stocksMapper.insert(stocksEntity);

        // 插入交易流水
        TradeRecordDo entity = new TradeRecordDo();
        entity.setDate(date);
        entity.setStockCode(stockCode);
        entity.setAmount(amount);
        entity.setDirection(1);
        entity.setPrice(price);
        tradeRecordMapper.insert(entity);
    }


}
