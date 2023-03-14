package com.mfml.trader.server.core.strategy.service;

import com.mfml.trader.server.core.strategy.strategy.BOLLStrategy;
import com.mfml.trader.server.core.strategy.strategy.MAStrategy;
import com.mfml.trader.server.core.strategy.strategy.MAStrategy_V2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

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

    @Override
    public void buy(String date, String stockCode, Integer amount) {
        maStrategyV2.buy(date, stockCode, amount);
    }

    @Override
    public void sell() {

    }
}
