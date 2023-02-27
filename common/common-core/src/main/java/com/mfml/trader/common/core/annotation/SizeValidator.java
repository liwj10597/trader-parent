package com.mfml.trader.common.core.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

public class SizeValidator implements ConstraintValidator<Size, Collection> {

    private Integer max;
    private Integer min;

    @Override
    public void initialize(Size obj) {
        max = obj.max();
        min = obj.min();
    }

    @Override
    public boolean isValid(Collection value, ConstraintValidatorContext context) {
        if (min == 0 && (null == value || value.isEmpty())) {
            return true;
        }
        if (null == value || value.isEmpty()) {
            return false;
        }

        return value.size() <= max && value.size() >= min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

}
