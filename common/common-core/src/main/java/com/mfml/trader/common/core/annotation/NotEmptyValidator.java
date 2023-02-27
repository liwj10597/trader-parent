package com.mfml.trader.common.core.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

/**
 *
 */
public class NotEmptyValidator implements ConstraintValidator<NotEmpty,Collection> {
    @Override
    public void initialize(NotEmpty constraintAnnotation) {

    }

    @Override
    public boolean isValid(Collection value, ConstraintValidatorContext context) {
        return (null == value || value.isEmpty()) ? false : true;
    }
}