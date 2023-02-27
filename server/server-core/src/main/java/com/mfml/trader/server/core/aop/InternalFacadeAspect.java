package com.mfml.trader.server.core.aop;

import com.alibaba.fastjson.JSON;
import com.mfml.trader.common.core.exception.TraderException;
import com.mfml.trader.common.core.result.CodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import static com.mfml.trader.common.core.result.ResultUtil.fail;

@Component
@Aspect
@Slf4j
public class InternalFacadeAspect {

    @Pointcut("execution(* com.mfml.trader.server.core.controller.internal..*Controller.*(..))")
    private void facadePointCut() {
    }

    @Around("facadePointCut()")
    public Object aroundController(ProceedingJoinPoint joinPoint) throws Throwable {
        return around(joinPoint);
    }

    /**
     * 环绕通知,不代理非public和返回类型不是APIResult的方法
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method realMethod = joinPoint.getTarget().getClass().getDeclaredMethod(
                joinPoint.getSignature().getName(),
                ((MethodSignature) joinPoint.getSignature()).getParameterTypes()
        );
        //不代理非public方法
        if (!Modifier.isPublic(realMethod.getModifiers())) {
            return joinPoint.proceed();
        }

        return doAround(joinPoint);
    }

    /**
     * 执行通知, 主要是统一异常处理,日志打印
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    private Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = null;
        try {
            result = joinPoint.proceed();
        }
        // 预期异常
        catch (TraderException e) {
            doErrorLog(joinPoint, e);
            result = processErrorResult(e);
        }
        //非预期异常
        catch (Exception ex) {
            doErrorLog(joinPoint, ex);
            result = processErrorResult(ex);
        } finally {
            doMonitorLog(joinPoint, result, start);
        }
        return result;
    }

    /**
     * 统一异常日志打印
     *
     * @param joinPoint
     * @param ex
     */
    private void doErrorLog(ProceedingJoinPoint joinPoint, Throwable ex) {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        String targetMethodDesc = joinPoint.getSignature().getName();
        String paramsString = parseParams(joinPoint.getArgs());
        //非预期异常打印error堆栈
        logger.error("{}({}) error", targetMethodDesc, paramsString, ex);
    }

    /**
     * 统一监控日志打印
     *
     * @param joinPoint
     * @param result
     * @param start
     */
    private void doMonitorLog(ProceedingJoinPoint joinPoint, Object result, long start) {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        String targetMethodDesc = joinPoint.getSignature().getName();
        String paramsString = parseParams(joinPoint.getArgs());
        // debug 关闭并且入参过长，截取前10000
        if (!log.isDebugEnabled() && paramsString.length() > 10000) {
            paramsString = paramsString.substring(0, 10000);
        }
        // debug 打开 打印完整结果集，否则不打印结果集
        String info = String.join("|", targetMethodDesc, "cost:" + (System.currentTimeMillis() - start),
                "params:" + paramsString, "debug:" + log.isDebugEnabled());
        if (log.isDebugEnabled()) {
            info = String.join("|", info, "result:" + result);
        }
        logger.info(info);
    }

    /**
     * 对异常结果统一处理
     */
    private Object processErrorResult(Exception e) {
        return fail(CodeUtil.FAILED);
    }

    private Object processErrorResult(TraderException e) {
        return fail(e.getCode(), e.getMessage());
    }

    /**
     * 解析入参
     *
     * @param params
     * @return
     */
    private String parseParams(Object[] params) {
        if (params != null && params.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (Object p : params) {
                if (p != null) {
                    sb.append(JSON.toJSONString(p) + ",");
                } else {
                    sb.append("null,");
                }
            }
            String res = sb.toString();
            if (res.length() > 1) {
                return res.substring(0, res.length() - 1);
            }
        }
        return "";
    }

}
