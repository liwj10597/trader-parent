package com.mfml.trader.server.core.controller.chatgpt;

import com.mfml.trader.common.core.annotation.ApiScan;
import com.mfml.trader.common.core.result.Result;
import com.mfml.trader.server.core.service.pay.PayFacade;
import com.mfml.trader.server.core.service.pay.ro.AlipayTradeAppPayRo;
import com.mfml.trader.server.core.service.pay.ro.AppProduceRo;
import com.mfml.trader.server.core.service.pay.ro.PayValidationRo;
import com.mfml.trader.server.core.service.pay.ro.SecretProduceRo;
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
@RestController
public class PayController {
    @Resource
    PayFacade payFacade;

    /**
     * 查询许可过期时间
     * @param ro
     * @return
     */
    @PostMapping(value = "queryValidation")
    @ResponseBody
    public Result<String> queryValidation(@RequestBody PayValidationRo ro) {
        return payFacade.queryValidation(ro);
    }


    /**
     * 验证许可是否过期，PC、APP均适用
     * @param ro
     * @return
     */
    @PostMapping(value = "payValidation")
    @ResponseBody
    public Result<Integer> payValidation(@RequestBody PayValidationRo ro) {
        return payFacade.payValidation(ro);
    }

    /**
     * 用于PC端批量生成许可
     * @param ro
     * @return
     */
    @PostMapping(value = "secretProduce")
    @ResponseBody
    public Result<List<String>> secretProduce(@RequestBody SecretProduceRo ro) {
        return payFacade.secretProduce(ro);
    }

    /**
     * 用于APP端支付成功后生成许可
     * @param ro
     * @return
     */
    @PostMapping(value = "appProduce")
    @ResponseBody
    public Result<Boolean> appProduce(@RequestBody AppProduceRo ro) {
        return payFacade.appProduce(ro);
    }

    /**
     * 获取支付宝订单信息
     * @param ro
     * @return
     */
    @PostMapping(value = "alipayTradeAppPay")
    @ResponseBody
    public Result<String> alipayTradeAppPay(@RequestBody AlipayTradeAppPayRo ro) {
        return payFacade.alipayTradeAppPay(ro);
    }
}
