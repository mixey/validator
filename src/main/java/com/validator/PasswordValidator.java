package com.validator;

public class PasswordValidator extends BaseValidator<String[]> {
    private final int PASSWORD_LENGTH = 5;

    private final String DEFAULT_ERROR_MESSAGE = "Пароль должен быть больше %d символов";
    private final String DEFAULT_ERROR_MESSAGE2 = "Пароли не совпадают";

    public PasswordValidator(int id) {
        super(id);
    }

    @Override
    public boolean validate() {
        String[] values = getValue();
        if (values[0].length() <= PASSWORD_LENGTH) {
            errorMessage = String.format(DEFAULT_ERROR_MESSAGE, PASSWORD_LENGTH);
            return false;
        }

        if (values.length > 1 && values[1] != null) {
            if (!values[1].equals(values[0])) {
                errorMessage = DEFAULT_ERROR_MESSAGE2;
                return false;
            }
        }

        return true;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
