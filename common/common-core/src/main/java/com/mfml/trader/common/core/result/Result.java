package com.mfml.trader.common.core.result;

import com.mfml.trader.common.core.model.base.ToString;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: caozhou
 * @create: 2022-07-15 14:24
 * @description: 统一返回结果类，用于接口返回值的封装
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Result<T> extends ToString {
    /**
     * 结果数据
     */
    private T data;

    /**
     * 状态码
     */
    private Code code;

    /**
     * 前端展示信息
     */
    private String appMsg;

    /**
     * 是否处理成功, 意味着没有业务失败和系统失败
     * @return
     */
    public boolean isSuccess() {
        return code.getCode() >= 0;
    }

    public boolean isFailed() {
        return !isSuccess();
    }
}
