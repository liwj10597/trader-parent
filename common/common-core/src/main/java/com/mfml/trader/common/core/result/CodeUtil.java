package com.mfml.trader.common.core.result;

/**
 * @author caozhou
 * @create 2022-07-15 14:51
 * @description 状态码工具类
 */
public class CodeUtil {
    public static final Code SUCCESS = new Code(0, "成功");

    public static final Code FAILED = new Code(-100,"请求失败");
    public static final Code ILLEGAL_PARAMS = new Code(-102,"无效的参数");
    public static final Code ILLEGAL_TOKEN = new Code(-103,"非法的token");
    public static final Code ILLEGAL_TEMPLATE = new Code(-104,"非法的模板");
    public static final Code ILLEGAL_SECRET = new Code(-105,"非法的secret");
    public static final Code UPLOAD_ERROR = new Code(-106,"上传失败");

    public static final Code PARSE_ERROR = new Code(-301,"解析失败");
    public static final Code ILLEGAL_SIGN = new Code(-302,"无效的签名");

    public static final Code DB_ERROR = new Code(-900,"数据库异常");
    public static final Code DUPLICATE_ERROR = new Code(-901,"主键冲突");
    public static final Code NOT_EXISTS = new Code(-902,"表不存在");

}
