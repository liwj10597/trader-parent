package com.mfml.trader.common.core.exception;

import com.mfml.trader.common.core.result.Code;
import com.mfml.trader.common.core.result.CodeUtil;
import lombok.Getter;

/**
 * @author: caozhou
 * @create: 2022-07-15 15:26
 * @description:鲸鱼系统异常处理类
 */
@Getter
public class TraderException extends RuntimeException {
    private final Code code;

    public TraderException(Code code) {
        super(code.getDesc());
        this.code = code;
    }

    public TraderException(String message) {
        super(message);
        this.code = CodeUtil.FAILED;
    }

    public TraderException(Code code, String message) {
        super(message);
        this.code = code;
    }

    public TraderException(Code code, Throwable cause) {
        super(code.getDesc(), cause);
        this.code = code;
    }

    public TraderException(String message, Throwable cause) {
        super(message, cause);
        this.code = CodeUtil.FAILED;
    }

    public TraderException(Throwable cause) {
        super(cause);
        this.code = CodeUtil.FAILED;
    }












































}
