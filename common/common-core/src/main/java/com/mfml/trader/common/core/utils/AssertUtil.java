package com.mfml.trader.common.core.utils;

import com.mfml.trader.common.core.exception.TraderException;
import com.mfml.trader.common.core.result.Code;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;


public class AssertUtil {
    private AssertUtil() {
    }

    public static void checkArgument(boolean expression, Code code) {
        if (!expression) {
            throw new TraderException(code);
        }
    }

    public static void checkArgument(boolean expression, String message) {
        if (!expression) {
            throw new TraderException(message);
        }
    }

    public static <T> void checkNotNull(T reference, Code code) {
        if (reference == null) {
            throw new TraderException(code);
        }
    }

    public static <T> void checkNotNull(T reference, String message) {
        if (reference == null) {
            throw new TraderException(message);
        }
    }

    public static void checkNotBlank(String str, Code code) {
        if (StringUtils.isBlank(str)) {
            throw new TraderException(code);
        }
    }

    public static void checkNotBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new TraderException(message);
        }
    }

    public static void checkNotEmpty(Collection clt, Code code) {
        if (CollectionUtils.isEmpty(clt)) {
            throw new TraderException(code);
        }
    }

    public static void checkNotEmpty(Collection clt, String message) {
        if (CollectionUtils.isEmpty(clt)) {
            throw new TraderException(message);
        }
    }

    public static void checkNotEmpty(Map map, Code code) {
        if (CollectionUtils.isEmpty(map)) {
            throw new TraderException(code);
        }
    }

    public static void checkNotEmpty(Map map, String message) {
        if (CollectionUtils.isEmpty(map)) {
            throw new TraderException(message);
        }
    }
}