package com.mfml.trader.server.core.controller.interceptor;

import com.alibaba.fastjson.JSON;
import com.mfml.trader.common.core.result.CodeUtil;
import com.mfml.trader.common.core.result.Result;
import com.mfml.trader.common.core.result.ResultUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 定义安全拦截器
 */
@Component
public class SecretInterceptor implements AsyncHandlerInterceptor {



    @Value("${trader.secret.switch:false}")
    private Boolean whaleSecretSwitch;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(Objects.equals(false, whaleSecretSwitch)){
            return true;
        }
        response.setContentType("application/json");
        Result<Object> result = ResultUtil.fail(CodeUtil.ILLEGAL_TOKEN, "非法的token");
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.write(JSON.toJSONBytes(result));
        }
        return false;
    }
}
