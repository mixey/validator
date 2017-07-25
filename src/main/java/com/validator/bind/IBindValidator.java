package com.validator.bind;

import com.validator.IFieldWrapper;
import com.validator.ValidatorGroup;

import java.util.Map;

public interface IBindValidator {
    ValidatorGroup getValidator();

    void add(IFieldWrapper wrapper);

    void enableAll();

    void disable(int[] fieldIds);

    void setErrors(Map<String, String> errors);

    IFieldWrapper getFieldWrapper(int fieldId);

    IFieldWrapper getFieldWrapper(String field);
}
