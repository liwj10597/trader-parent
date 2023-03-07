package com.mfml.trader.common.core.enums;

import lombok.AllArgsConstructor;

/**
 * 复权类型
 * before 前复权； after 后复权；normal 不复权
 * @author caozhou
 * @date 2023-03-01 16:22
 */
@AllArgsConstructor
public enum Recovery {
    before("before", "前复权"),
    after("after", "后复权"),
    normal("normal","不复权");

    public  String code;

    public String desc;
}
