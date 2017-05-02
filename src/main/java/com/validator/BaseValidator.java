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

    protected String convertToUTF(String win1251Value) {
        try {
            return new String(win1251Value.getBytes("windows-1251"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getErrorMessage() {
        return convertToUTF(errorMessage);
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
