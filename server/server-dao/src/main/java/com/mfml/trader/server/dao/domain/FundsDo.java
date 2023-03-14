package com.mfml.trader.server.dao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mfml.trader.server.dao.BaseDo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author caozhou
 * @date 2023-03-14 15:29
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "funds", autoResultMap = true)
public class FundsDo extends BaseDo {
    /**
     * 自增长id,
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 日期,
     */
    private String date;

    /**
     * 资金 单位 元,
     */
    private Double fundsAmount;
}
