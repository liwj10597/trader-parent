package com.mfml.trader.server.core.pay.ro;

import com.mfml.trader.common.core.model.base.ToString;
import lombok.Data;

/**
 * @author caozhou
 * @date 2023-03-27 16:40
 */
@Data
public class PayValidationRo extends ToString {
    private String secretKey;
}
