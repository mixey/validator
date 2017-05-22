package com.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameValidator extends BaseValidator<String> {
    private final String PATTERN = "^[a-zA-ZА-Яа-я]+$";
    private final String DEFAULT_ERROR_MESSAGE = "Пустое или неверное значение";

    public NameValidator(int id) {
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
