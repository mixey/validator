package com.validator.bind;

import java.lang.reflect.Constructor;

public class BindValidator {

    public static IBindValidator bind(Object target) {
        try {
            Class<?> clazz = Class.forName(target.getClass().getName() + "_BindValidator");
            Constructor<?> constructor = clazz.getConstructor(Object.class);
            return (IBindValidator) constructor.newInstance(target);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
