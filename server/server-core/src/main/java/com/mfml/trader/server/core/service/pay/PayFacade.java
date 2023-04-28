package com.mfml.trader.server.core.service.pay;

import com.mfml.trader.common.core.result.Result;
import com.mfml.trader.server.core.service.pay.ro.AlipayTradeAppPayRo;
import com.mfml.trader.server.core.service.pay.ro.PayValidationRo;
import com.mfml.trader.server.core.service.pay.ro.SecretProduceRo;
import com.mfml.trader.server.core.service.pay.ro.AppProduceRo;

import java.util.List;

/**
 * @author caozhou
 * @date 2023-03-27 16:18
 */
public interface PayFacade {

    /**
     * 查询许可过期时间
     * @return true 表示验证成功；false表示验证失败
     */
    Result<String> queryValidation(PayValidationRo ro);

    /**
     * 验证许可是否过期，PC、APP均适用
     * @param ro
     * @return true 表示验证成功；false表示验证失败
     */
    Result<Integer> payValidation(PayValidationRo ro);

    /**
     * 生成秘钥
     */
    Result<List<String>> secretProduce(SecretProduceRo ro);

    /**
     * APP生成许可
     */
    Result<Boolean> appProduce(AppProduceRo ro);

    /**
     * 生成支付宝订单
     */
    Result<String> alipayTradeAppPay(AlipayTradeAppPayRo ro);
}
