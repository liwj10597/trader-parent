package com.mfml.trader.server.core.pay;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.mfml.trader.common.core.result.CodeUtil;
import com.mfml.trader.common.core.result.Result;
import com.mfml.trader.common.core.result.ResultUtil;
import com.mfml.trader.common.dao.domain.SecretVerificationDo;
import com.mfml.trader.common.dao.mapper.SecretVerificationMapper;
import com.mfml.trader.server.core.pay.ro.PayValidationRo;
import com.mfml.trader.server.core.pay.ro.SecretProduceRo;
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
}
