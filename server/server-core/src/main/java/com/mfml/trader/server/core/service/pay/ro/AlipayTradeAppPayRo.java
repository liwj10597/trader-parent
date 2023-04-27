package com.mfml.trader.server.core.service.pay.ro;

import com.mfml.trader.common.core.annotation.NotBlank;
import com.mfml.trader.common.core.model.base.ToString;
import lombok.Data;

/**
 * @author caozhou
 * @date 2023-04-27 10:49
 */
@Data
public class AlipayTradeAppPayRo extends ToString {
    /**
     * 订单总金额
     * 必填
     */
    private Double totalAmount;
    /**
     * 订单标题
     * 必填
     */
    @NotBlank
    private String subject;
}
