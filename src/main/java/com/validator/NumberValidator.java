package com.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberValidator extends BaseValidator<String> {
    private final String PATTERN = "^\\d+$";
    private final String DEFAULT_ERROR_MESSAGE = "Неверный формат";

    public NumberValidator(int id) {
        super(id);
        errorMessage = DEFAULT_ERROR_MESSAGE;
    }

    @Override
    public boolean validate() {
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(getValue());
        if (!matcher.matches()) {
            return false;
        }

        return true;
    }
}
