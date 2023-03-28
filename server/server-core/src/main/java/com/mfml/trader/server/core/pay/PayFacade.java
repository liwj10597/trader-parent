package com.mfml.trader.server.core.pay;

import com.mfml.trader.common.core.result.Result;
import com.mfml.trader.server.core.pay.ro.PayValidationRo;
import com.mfml.trader.server.core.pay.ro.SecretProduceRo;

import java.util.List;

/**
 * @author caozhou
 * @date 2023-03-27 16:18
 */
public interface PayFacade {

    /**
     * 秘钥验证
     * @param ro
     * @return true 表示验证成功；false表示验证失败
     */
    Result<Boolean> payValidation(PayValidationRo ro);

    /**
     * 生成秘钥
     */
    Result<List<String>> secretProduce(SecretProduceRo ro);
}
