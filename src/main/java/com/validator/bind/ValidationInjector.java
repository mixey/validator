package com.validator.bind;

import java.lang.reflect.Constructor;

public class ValidationInjector {

    public static IBindValidator inject(Object target) {
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
