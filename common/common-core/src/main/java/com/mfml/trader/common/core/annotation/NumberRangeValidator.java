package com.mfml.trader.common.core.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NumberRangeValidator implements ConstraintValidator<NumberRange, Integer> {

    private Double max;
    private Double min;
    private boolean required;

    @Override
    public void initialize(NumberRange obj) {
        max = obj.max();
        min = obj.min();
        required = obj.required();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (required && null == value) {
            return false;
        }
        if (null == value) {
            return true;
        }
        return value <= max && value >= min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
