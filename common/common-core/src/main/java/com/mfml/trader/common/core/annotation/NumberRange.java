package com.mfml.trader.common.core.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NumberRangeValidator.class)
public @interface NumberRange {

    double min();

    double max();

    boolean required() default false;

    String message() default "不在允许范围内";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
