package com.validator;

public class CheckValidator extends BaseValidator<Boolean> {
    private final String DEFAULT_ERROR_MESSAGE = "Обязательное поле";

    public CheckValidator(int id) {
        super(id);
        errorMessage = DEFAULT_ERROR_MESSAGE;
    }

    @Override
    public boolean validate() {
        if (!getValue()) {
            return false;
        }

        return true;
    }
}

