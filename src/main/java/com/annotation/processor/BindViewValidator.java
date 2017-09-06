package com.annotation.processor;

import com.validator.ValidatorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface BindViewValidator {
    String field() default "";

    String type() default ValidatorType.REQUIRED;

    int errorMessage() default 0;

    String related() default "";

    String pattern() default "";
}
