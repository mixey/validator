package com.validator;

public interface IFieldWrapper {
    Object getValue();

    IValidator getValidator();

    void setError(String message);

    boolean isActiveValidator();

    String getField();

    void addPlugin(IFieldPlugin plugin);
}
