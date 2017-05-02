package com.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneValidator extends BaseValidator<String> {
    private final String PATTERN = "[78]\\d{10}";
    private final String DEFAULT_ERROR_MESSAGE = "Неправильный формат телефона";

    public PhoneValidator(int id) {
        super(id);
    }

    @Override
    public boolean validate() {
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(getValue());
        if (!matcher.matches()) {
            errorMessage = DEFAULT_ERROR_MESSAGE;
            return false;
        }

        return true;
    }

    public String getErrorMessage() {
        return convertToUTF(errorMessage);
    }
}
