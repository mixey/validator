package com.validator;

@Deprecated
/**
 * @deprecated use {@link IFieldWrapper} instead
 * */
public interface IFieldExtension {
    Object getValue();

    IValidator getValidator();

    void setError(String message);

    boolean isActiveValidator();

    String getField();
}
