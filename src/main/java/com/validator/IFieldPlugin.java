package com.validator;

public interface IFieldPlugin {
    Object getValue();

    boolean setError(String message);
}
