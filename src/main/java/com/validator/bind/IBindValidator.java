package com.validator.bind;

import com.validator.ValidatorGroup;

import java.util.Map;

public interface IBindValidator {
    ValidatorGroup getValidator();

    void enableAll();

    void disable(int[] ids);

    void setErrors(Map<String, String> errors);
}
