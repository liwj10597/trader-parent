package com.mfml.trader.common.core.utils;

import com.mfml.trader.common.core.exception.TraderException;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.validator.HibernateValidator;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidateUtil {
    private static final Validator validator = Validation.byProvider(HibernateValidator.class)
            .configure().failFast(true).buildValidatorFactory().getValidator();

    private ValidateUtil() {
    }

    public static <T> void validOrThrowException(T bean, Class<?>... groups) {
        if (groups.length > 0) {
            groups = ArrayUtils.add(groups, Default.class);
        }
        Set<ConstraintViolation<T>> violations = validator.validate(bean, groups);
        if (!violations.isEmpty()) {
            throw new TraderException(buildMessageFromViolations(violations));
        }
    }

    public static <T> String validOrReturnMsg(T bean, Class<?>... groups) {
        if (groups.length > 0) {
            groups = ArrayUtils.add(groups, Default.class);
        }
        Set<ConstraintViolation<T>> violations = validator.validate(bean, groups);
        if (!violations.isEmpty()) {
            return buildMessageFromViolations(violations);
        }
        return null;
    }

    public static <T> Set<ConstraintViolation<T>> validOrReturnViolations(T bean, Class<?>... groups) {
        if (groups.length > 0) {
            groups = ArrayUtils.add(groups, Default.class);
        }
        return validator.validate(bean, groups);
    }

    private static <T> String buildMessageFromViolations(Set<ConstraintViolation<T>> violations) {
        return String.join(",", violations.stream().map(
                violation -> violation.getPropertyPath().toString() + " " + violation.getMessage()
        ).collect(Collectors.toList()));
    }
}
