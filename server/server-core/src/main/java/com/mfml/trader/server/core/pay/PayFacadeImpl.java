package com.mfml.trader.server.core.pay;

import com.mfml.trader.common.core.exception.TraderException;
import com.mfml.trader.common.core.result.Result;
import com.mfml.trader.server.core.pay.ro.PayValidationRo;
import com.mfml.trader.server.core.pay.ro.SecretProduceRo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

/**
 * @author caozhou
 * @date 2023-03-27 16:18
 */
@Slf4j
@Service
public class PayFacadeImpl implements PayFacade {

    @Override
    public Result<Boolean> payValidation(PayValidationRo ro) {
        return null;
    }

    @Override
    public Result<List<String>> secretProduce(SecretProduceRo ro) {
        if (null == ro.getDays() || ro.getDays() <= 0) {
            throw new TraderException("入参days无效");
        }
        String uuid = UUID.randomUUID().toString();

        return null;
    }
}
