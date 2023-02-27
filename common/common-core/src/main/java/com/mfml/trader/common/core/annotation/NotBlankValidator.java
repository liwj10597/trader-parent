package com.mfml.trader.common.core.annotation;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author: 苏释
 * @create: 2020年10月16日 17:34
 * @description:
 */
public class NotBlankValidator implements ConstraintValidator<NotBlank,String> {
    @Override
    public void initialize(NotBlank constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.isNotBlank(value);
    }
}