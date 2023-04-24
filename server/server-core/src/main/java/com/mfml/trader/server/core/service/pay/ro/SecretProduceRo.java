package com.mfml.trader.server.core.service.pay.ro;

import com.mfml.trader.common.core.annotation.NumberRange;
import com.mfml.trader.common.core.model.base.ToString;
import lombok.Data;

/**
 * @author caozhou
 * @date 2023-03-27 17:06
 */
@Data
public class SecretProduceRo extends ToString {
    /**
     * 有效期天数 1 表示一天 7表示7天，以此类推
     */
    @NumberRange(min = 1, max = 365, required = true)
    private Integer days;

    /**
     * 密钥个数
     */
    @NumberRange(min = 1, max = 100, required = true)
    private Integer count;
}
