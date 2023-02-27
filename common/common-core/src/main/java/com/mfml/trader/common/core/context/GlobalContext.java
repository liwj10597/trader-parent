package com.mfml.trader.common.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 应用上下文
 *
 * @author caozhou
 */
@Slf4j
public class GlobalContext {
    public static final String EMP_NO = "empNo";
    public static final String EMP_NAME = "empName";
    public static final String COMPANY_ID = "companyId";
    public static final String COMPANY_NAME = "companyName";

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static void clearAll() {
        THREAD_LOCAL.remove();
    }

    /**
     * 当前登陆用户编号
     * @return 如果没有，返回null
     */
    public static String getEmpNo() {
        Object obj = get(EMP_NO);
        if (Objects.isNull(obj)) {
            return null;
        }
        return String.valueOf(obj);
    }

    public static String getEmpName() {
        Object obj = get(EMP_NAME);
        if (Objects.isNull(obj)) {
            return null;
        }
        return String.valueOf(obj);
    }

    public static String getCompanyId() {
        Object obj = get(COMPANY_ID);
        if (Objects.isNull(obj)) {
            return null;
        }
        return String.valueOf(obj);
    }

    public static String getCompanyName() {
        Object obj = get(COMPANY_NAME);
        if (Objects.isNull(obj)) {
            return null;
        }
        return String.valueOf(obj);
    }



    public static void set(String key, Object value) {
        if (value == null) {
            return;
        }
        Map<String, Object> contextMap = THREAD_LOCAL.get();
        if (contextMap == null) {
            contextMap = new ConcurrentHashMap<>(16);
        }
        contextMap.put(key, value);
        THREAD_LOCAL.set(contextMap);
    }

    public static Object get(String key) {
        Map<String, Object> contextMap = THREAD_LOCAL.get();
        if (contextMap == null) {
            return null;
        }
        return contextMap.get(key);
    }

}
