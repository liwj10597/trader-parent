package com.mfml.trader.common.core.result;

import com.mfml.trader.common.core.model.base.ToString;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: caozhou
 * @create: 2022-07-15 14:41
 * @description:状态码
 */
@Data
@Accessors
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Code extends ToString {
    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态描述
     */
    private String desc;
}
