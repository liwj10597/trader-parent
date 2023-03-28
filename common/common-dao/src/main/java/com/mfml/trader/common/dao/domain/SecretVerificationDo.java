package com.mfml.trader.common.dao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mfml.trader.common.dao.domain.base.BaseDo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author caozhou
 * @date 2023-03-28 09:31
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "secret_verification", autoResultMap = true)
public class SecretVerificationDo extends BaseDo {
    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 密钥有效期天数
     */
    private Integer secretDays;

    /**
     * 密钥生成日期 yyyy-MM-dd
     */
    private String produceDate;

    /**
     * 核销开始时间 格式yyyy-MM-dd hh:mm:ss
     */
    private String verifyBeginDatetime;

    /**
     * 核销结束时间 格式yyyy-MM-dd hh:mm:ss
     */
    private String verifyEndDatetime;
}
