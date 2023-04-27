package com.mfml.trader.server;

import com.mfml.trader.common.core.result.Result;
import com.mfml.trader.server.core.service.pay.PayFacade;
import com.mfml.trader.server.core.service.pay.ro.AlipayTradeAppPayRo;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;

/**
 * @author caozhou
 * @date 2023-04-27 11:07
 */
@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlipayTest extends BaseTest{
    @Resource
    PayFacade payFacade;


    @Test
    public void alipayTest() {
        AlipayTradeAppPayRo ro = new AlipayTradeAppPayRo();
        ro.setSubject("测试支付");
        ro.setTotalAmount(1D);
        Result<String> result = payFacade.alipayTradeAppPay(ro);
        System.out.println(result);
    }
}