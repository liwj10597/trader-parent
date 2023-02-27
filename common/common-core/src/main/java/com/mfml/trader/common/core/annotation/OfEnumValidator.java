package com.mfml.trader.common.core.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OfEnumValidator implements ConstraintValidator<OfEnum, Object> {

    private Object[] values;

    @Override
    public void initialize(OfEnum ofEnum) {
        Class<?> clz = ofEnum.value();
        values = clz.getEnumConstants();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (! (value instanceof String)){
            return false;
        }
        for (Object v:values){
            Enum e =(Enum) v;
            if (e.name().equals(value)){
                return true;
            }
        }
        return false;
    }
}