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
@TableName(value = "stocks", autoResultMap = true)
public class StocksDo extends BaseDo {
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
     * 买入日期
     */
    private String buyDate;

    /**
     * 股票代码,
     */
    private String stockCode;

    /**
     * 持仓数量 单位 股,
     */
    private Integer stockAmount;

    /**
     * 持仓成本,
     */
    private Double costPrice;
}
