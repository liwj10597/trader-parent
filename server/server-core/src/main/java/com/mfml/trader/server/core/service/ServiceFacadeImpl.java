package com.mfml.trader.server.core.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.mfml.trader.server.core.service.strategy.BOLLStrategy;
import com.mfml.trader.server.core.service.strategy.MAStrategy;
import com.mfml.trader.server.core.service.strategy.MAStrategy_V2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

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
        Date myDate = DateUtil.parse(date, DatePattern.NORM_DATE_PATTERN).toJdkDate();
        Calendar instance = Calendar.getInstance();
        instance.setTime(myDate);
        int i = instance.get(Calendar.DAY_OF_WEEK);
        if (i == 7 || i == 1) {
            return ;
        }

        //maStrategy.buy(date, stockCode, amount);
        //bollStrategy.buy(date, stockCode, amount);

        maStrategyV2.buy(date, stockCode, amount);
    }

    @Override
    public void sell() {

    }
}
