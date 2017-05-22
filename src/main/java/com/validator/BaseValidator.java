package com.validator;

import java.io.UnsupportedEncodingException;

public abstract class BaseValidator<T> implements IValidator<T> {
    protected final int id;
    protected T value;
    protected String errorMessage;

    public BaseValidator(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setErrorMessage(String value) {
        this.errorMessage = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
