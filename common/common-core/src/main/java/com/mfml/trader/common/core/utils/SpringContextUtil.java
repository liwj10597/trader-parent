package com.mfml.trader.common.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.Optional;

/**
 * @author: 曹州
 * @date: 2022/07/15 下午4:03
 */
public class SpringContextUtil implements ApplicationContextAware, EnvironmentAware {
    /**
     * 上下文对象实例
     */
    private static ApplicationContext applicationContext;

    /**
     * environment对象实例
     */
    private static Environment environment;

    /**
     * 获取environment
     *
     * @return
     */
    public static Environment getEnvironment() {
        return environment;
    }

    @Override
    public void setEnvironment(Environment environment) {
        SpringContextUtil.environment = environment;
    }

    /**
     * 获取applicationContext
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * 通过name获取 Bean.
     *
     * @param name
     * @return
     */
    public static <T> T getBean(String name) {
        return (T)getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean.
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * 获取配置
     *
     * @param key
     * @param targetType
     * @param <T>
     * @return
     */
    public static <T> T getProperty(String key, Class<T> targetType) {
        return Optional.ofNullable(getEnvironment()).map(env -> env.getProperty(key, targetType)).orElse(null);
    }

    /**
     * 获取配置
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getProperty(String key, String defaultValue) {
        return Optional.ofNullable(getEnvironment()).map(env -> env.getProperty(key, defaultValue)).orElse(defaultValue);
    }

    /**
     * 获取配置
     *
     * @param key
     * @param targetType
     * @param defaultValue
     * @param <T>
     * @return
     */
    public static <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return Optional.ofNullable(getEnvironment()).map(env -> env.getProperty(key, targetType, defaultValue)).orElse(defaultValue);
    }


}
