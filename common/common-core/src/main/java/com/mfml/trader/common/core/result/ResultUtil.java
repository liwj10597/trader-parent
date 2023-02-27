package com.mfml.trader.common.core.result;

/**
 * 统一返回体工具类
 * @author caozhou
 * @create 2022-07-15 14:51
 */
public class ResultUtil {
    public static <T> Result<T> success(T data) {
        return result(data, CodeUtil.SUCCESS, null);
    }

    public static <T> Result<T> success(T data, String appMsg) {
        return result(data, CodeUtil.SUCCESS, appMsg);
    }

    public static <T> Result<T> fail(Code code) {
        return result(null, code, null);
    }

    public static <T> Result<T> fail(Code code, String appMsg) {
        return result(null, code, appMsg);
    }

    public static <T> Result<T> result(T data, Code code, String appMsg) {
        return new Result<T>().setData(data).setCode(code).setAppMsg(appMsg);
    }
}
