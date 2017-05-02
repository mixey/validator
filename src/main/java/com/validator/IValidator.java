package com.validator;

public interface IValidator<T> {
    boolean validate();

    String getErrorMessage();

    void setErrorMessage(String value);

    int getId();

    void setValue(T text);
}
