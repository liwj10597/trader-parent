package com.mfml.trader.server.core.service.pay.ro;

import com.mfml.trader.common.core.annotation.NotBlank;
import com.mfml.trader.common.core.model.base.ToString;
import lombok.Data;

/**
 * @author caozhou
 * @date 2023-03-27 16:40
 */
@Data
public class PayValidationRo extends ToString {
    @NotBlank(message = "密钥不能为空")
    private String secretKey;
}
