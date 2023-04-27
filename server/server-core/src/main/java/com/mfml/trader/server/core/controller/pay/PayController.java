package com.mfml.trader.server.core.controller.pay;

import com.mfml.trader.common.core.annotation.ApiScan;
import com.mfml.trader.common.core.result.Result;
import com.mfml.trader.server.core.service.pay.PayFacade;
import com.mfml.trader.server.core.service.pay.ro.AlipayTradeAppPayRo;
import com.mfml.trader.server.core.service.pay.ro.AppProduceRo;
import com.mfml.trader.server.core.service.pay.ro.PayValidationRo;
import com.mfml.trader.server.core.service.pay.ro.SecretProduceRo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author caozhou
 * @date 2023-03-27 16:38
 */
@Slf4j
@ApiScan
@RestController
public class PayController {
    @Resource
    PayFacade payFacade;

    @ApiOperation(value = "秘钥验证", notes = "秘钥验证", tags = {"Pay"})
    @PostMapping(value = "payValidation")
    @ResponseBody
    public Result<Integer> payValidation(@RequestBody PayValidationRo ro) {
        return payFacade.payValidation(ro);
    }

    @ApiOperation(value = "秘钥生成", notes = "秘钥生成", tags = {"Pay"})
    @PostMapping(value = "secretProduce")
    @ResponseBody
    public Result<List<String>> secretProduce(@RequestBody SecretProduceRo ro) {
        return payFacade.secretProduce(ro);
    }

    @ApiOperation(value = "APP生成许可", notes = "APP生成许可", tags = {"Pay"})
    @PostMapping(value = "appProduce")
    @ResponseBody
    public Result<Boolean> appProduce(@RequestBody AppProduceRo ro) {
        return payFacade.appProduce(ro);
    }

    @ApiOperation(value = "支付宝APP支付订单生成", notes = "支付宝APP支付订单生成", tags = {"Pay"})
    @PostMapping(value = "alipayTradeAppPay")
    @ResponseBody
    public Result<String> alipayTradeAppPay(@RequestBody AlipayTradeAppPayRo ro) {
        return payFacade.alipayTradeAppPay(ro);
    }
}
