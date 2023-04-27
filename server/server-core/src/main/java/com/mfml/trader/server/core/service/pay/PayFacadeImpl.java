package com.mfml.trader.server.core.service.pay;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.mfml.trader.common.core.result.CodeUtil;
import com.mfml.trader.common.core.result.Result;
import com.mfml.trader.common.core.result.ResultUtil;
import com.mfml.trader.common.core.utils.SnowflakeUtil;
import com.mfml.trader.common.dao.domain.SecretVerificationDo;
import com.mfml.trader.common.dao.mapper.SecretVerificationMapper;
import com.mfml.trader.server.core.client.PayClient;
import com.mfml.trader.server.core.service.pay.ro.AlipayTradeAppPayRo;
import com.mfml.trader.server.core.service.pay.ro.PayValidationRo;
import com.mfml.trader.server.core.service.pay.ro.SecretProduceRo;
import com.mfml.trader.server.core.service.pay.ro.AppProduceRo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author caozhou
 * @date 2023-03-27 16:18
 */
@Slf4j
@Service
public class PayFacadeImpl implements PayFacade {

    @Resource
    PayClient payClient;

    @Resource
    SecretVerificationMapper secretVerificationMapper;

    @Override
    public Result<Integer> payValidation(PayValidationRo ro) {
        try {
            SecretVerificationDo svdo = secretVerificationMapper.selectOne(new LambdaQueryWrapper<SecretVerificationDo>().eq(SecretVerificationDo::getSecretKey, ro.getSecretKey()));
            if (null == svdo) {
                return ResultUtil.result(0, CodeUtil.ILLEGAL_SECRET, String.join(",", CodeUtil.ILLEGAL_SECRET.getDesc()));
            }
            String beginTime = svdo.getVerifyBeginDatetime();
            String endTime = svdo.getVerifyEndDatetime();
            if (null == beginTime || null == endTime) {

                beginTime = DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN);
                endTime = DateUtil.format(DateUtil.offsetDay(new Date(), svdo.getSecretDays()), DatePattern.NORM_DATETIME_PATTERN);

                svdo.setVerifyBeginDatetime(beginTime);
                svdo.setVerifyEndDatetime(endTime);
                svdo.setUpdateTime(new Date());
                secretVerificationMapper.updateById(svdo);
            }

            long currentTime = new Date().getTime();
            long endT = DateUtil.parse(endTime, DatePattern.NORM_DATETIME_PATTERN).getTime();
            if (currentTime > endT) {
                return ResultUtil.result(0, CodeUtil.ILLEGAL_SECRET, String.join(",", CodeUtil.ILLEGAL_SECRET.getDesc(), "secret已过期"));
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ResultUtil.result(0, CodeUtil.FAILED, String.join(",", CodeUtil.FAILED.getDesc(), "请联系管理员"));
        }
        return ResultUtil.success(1);
    }

    @Override
    public Result<List<String>> secretProduce(SecretProduceRo ro) {
        List<String> secrets = Lists.newArrayList();
        for (int idx = 0; idx < ro.getCount(); idx++) {
            String produceDate = DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN);
            String secret = UUID.randomUUID().toString();
            Integer days = ro.getDays();

            SecretVerificationDo entity = new SecretVerificationDo();
            entity.setProduceDate(produceDate);
            entity.setSecretKey(secret);
            entity.setSecretDays(days);
            secretVerificationMapper.insert(entity);

            secrets.add(secret);
        }
        return ResultUtil.success(secrets);
    }

    @Override
    public Result<Boolean> appProduce(AppProduceRo ro) {
        try {
            // 验签(预留)

            // 查支付宝订单(预留)

            // 生成许可
            String produceDate = DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN);
            Integer days = ro.getDays();
            SecretVerificationDo entity = new SecretVerificationDo();
            entity.setProduceDate(produceDate);
            entity.setSecretKey(ro.getUid());
            entity.setSecretDays(days);
            secretVerificationMapper.insert(entity);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ResultUtil.fail(CodeUtil.DB_ERROR);
        }
        return ResultUtil.success(true);
    }

    @Override
    public Result<String> alipayTradeAppPay(AlipayTradeAppPayRo ro) {
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", String.valueOf(SnowflakeUtil.nextId()));
        bizContent.put("total_amount", ro.getTotalAmount());
        bizContent.put("subject", ro.getSubject());
        bizContent.put("time_expire", DateUtil.format(DateUtil.offsetMinute(new Date(), 30).toJdkDate(), DatePattern.NORM_DATETIME_PATTERN));
        request.setBizContent(bizContent.toString());
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = payClient.getClientBT().sdkExecute(request);
            return ResultUtil.success(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
        } catch (AlipayApiException e) {
            log.warn("alipayTradeAppPay error ", e);
        }
        return ResultUtil.fail(CodeUtil.FAILED);
    }
}
