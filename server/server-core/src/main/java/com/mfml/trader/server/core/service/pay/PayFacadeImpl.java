package com.mfml.trader.server.core.service.pay;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
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
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
    public Result<String> queryValidation(PayValidationRo ro) {
        try {
            List<SecretVerificationDo> svdos = secretVerificationMapper.selectList(new LambdaQueryWrapper<SecretVerificationDo>().eq(SecretVerificationDo::getSecretKey, ro.getSecretKey()).orderByDesc(SecretVerificationDo::getUpdateTime));
            if (CollectionUtils.isEmpty(svdos)) {
                return ResultUtil.result("", CodeUtil.SUCCESS, "");
            }
            return ResultUtil.result(Optional.ofNullable(svdos.get(0).getVerifyEndDatetime()).orElse(""), CodeUtil.SUCCESS, "");
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ResultUtil.result("", CodeUtil.FAILED, String.join(",", CodeUtil.FAILED.getDesc(), "请联系管理员"));
        }
    }

    @Override
    public Result<Integer> payValidation(PayValidationRo ro) {
        try {
            List<SecretVerificationDo> svdos = secretVerificationMapper.selectList(new LambdaQueryWrapper<SecretVerificationDo>().eq(SecretVerificationDo::getSecretKey, ro.getSecretKey()).orderByDesc(SecretVerificationDo::getUpdateTime));
            if (CollectionUtils.isEmpty(svdos)) {
                return ResultUtil.result(0, CodeUtil.SUCCESS, "");
            }
            SecretVerificationDo svdo = svdos.get(0);
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
                return ResultUtil.result(0, CodeUtil.SUCCESS, "");
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
            List<SecretVerificationDo> list = secretVerificationMapper.selectList(new LambdaQueryWrapper<SecretVerificationDo>().eq(SecretVerificationDo::getSecretKey, ro.getUid()));
            if (CollectionUtils.isEmpty(list)) {
                SecretVerificationDo entity = new SecretVerificationDo();
                entity.setProduceDate(DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));
                entity.setSecretKey(ro.getUid());
                entity.setSecretDays(ro.getDays());
                entity.setVerifyBeginDatetime(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN));
                entity.setVerifyEndDatetime(DateUtil.format(DateUtil.offsetDay(new Date(), ro.getDays()), DatePattern.NORM_DATETIME_PATTERN));
                secretVerificationMapper.insert(entity);
            } else {
                SecretVerificationDo entity = list.get(0);
                String endTime = Optional.ofNullable(entity.getVerifyEndDatetime()).orElse("");
                String now = DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN);
                String r = endTime.compareTo(now) < 0 ? now : endTime;
                Date rDate = DateUtil.parse(r, DatePattern.NORM_DATETIME_PATTERN).offset(DateField.DAY_OF_YEAR, ro.getDays()).toJdkDate();
                entity.setVerifyEndDatetime(DateUtil.format(rDate, DatePattern.NORM_DATETIME_PATTERN));
                entity.setUpdateTime(new Date());
                secretVerificationMapper.updateById(entity);
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ResultUtil.fail(CodeUtil.DB_ERROR);
        }
        return ResultUtil.success(true);
    }

    public static void main(String[] args) {
        String endTime = "";
        String now = "21";
        System.out.println(endTime.compareTo(now));
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
