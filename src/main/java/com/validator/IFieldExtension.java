package com.validator;

public interface IFieldExtension {
    Object getValue();

    IValidator getValidator();

    void setError(String message);

    boolean isActiveValidator();

    String getField();
}
