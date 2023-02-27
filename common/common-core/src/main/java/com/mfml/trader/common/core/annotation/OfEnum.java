package com.mfml.trader.common.core.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 只用来校验String
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OfEnumValidator.class)
public @interface OfEnum {
    Class<?> value();
    String message() default "入参值不在正确枚举中";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};



}
