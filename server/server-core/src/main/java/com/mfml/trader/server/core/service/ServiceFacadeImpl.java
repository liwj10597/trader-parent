package com.mfml.trader.server.core.service;

import com.mfml.trader.server.core.service.strategy.BaseBOLLStrategy;
import com.mfml.trader.server.core.service.strategy.BaseMAStrategy;
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
    BaseMAStrategy baseMAStrategy;
   @Resource
 BaseBOLLStrategy baseBOLLStrategy;

    @Override
    public void buy(String date, String stockCode, Integer amount) {
        //baseMAStrategy.buy(date, stockCode, amount);
     baseBOLLStrategy.buy(date, stockCode, amount);
    }

    @Override
    public void sell() {

    }
}
