package com.validator;

public class RequiredValidator extends BaseValidator<String> {
    protected String field;
    private final String DEFAULT_ERROR_MESSAGE = "Обязательное поле";

    public RequiredValidator(int resId) {
        super(resId);
        errorMessage = DEFAULT_ERROR_MESSAGE;
    }

    @Override
    public boolean validate() {
        if (getValue() == null || getValue().isEmpty()) {
            return false;
        }

        return true;
    }
}
