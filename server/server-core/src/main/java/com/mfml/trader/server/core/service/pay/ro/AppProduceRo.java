package com.mfml.trader.server.core.service.pay.ro;

import com.mfml.trader.common.core.annotation.NotBlank;
import com.mfml.trader.common.core.annotation.NumberRange;
import com.mfml.trader.common.core.model.base.ToString;
import lombok.Data;

/**
 * @author caozhou
 * @date 2023-03-27 17:06
 */
@Data
public class AppProduceRo extends ToString {
    /**
     * app uid
     */
    @NotBlank
    private String uid;

    /**
     * 有效期天数 1 表示一天 7表示7天，以此类推
     */
    @NumberRange(min = 1, max = 365, required = true)
    private Integer days;

    /**
     * 支付宝订单号
     * 预留，查支付宝订单使用
     */
    private String tradeNo;

    /**
     * 签名
     * 预留，入参防篡改
     */
    private String sign;
}
