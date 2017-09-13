package com.annotation.processor;

import com.validator.ValidatorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface Rule {
    String field() default "";

    String type() default ValidatorType.REQUIRED;

    int errorMessage() default 0;

    int related() default 0;

    String pattern() default "";

    int view();
}
