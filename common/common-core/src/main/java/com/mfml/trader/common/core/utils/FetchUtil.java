package com.mfml.trader.common.core.utils;

import com.mfml.trader.common.core.exception.TraderException;
import com.mfml.trader.common.core.result.Result;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 请求模板
 */
@Slf4j
public class FetchUtil {

    public static <T> Optional<T> fetchOptional(Supplier<Result<T>> supplier) {
        try {
            Result<T> result = supplier.get();

            if (null == result || result.isFailed()) {
                throw new TraderException( null == result ? "result is null" : result.toString());
            }

            return Optional.ofNullable(result.getData());
        } catch (Throwable e) {
            log.error("Rpc invoke fail {}", e);
        }
        return Optional.empty();
    }

    public static <T> List<T> fetchCollection(Supplier<Result<List<T>>> supplier) {
        return fetchOptional(supplier).orElse(Collections.emptyList());
    }
}


















