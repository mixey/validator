package com.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomValidator extends BaseValidator<String> {
    private final String DEFAULT_ERROR_MESSAGE = "Обязательное поле";
    private final String regex;

    public CustomValidator(int id, String regex) {
        super(id);
        errorMessage = DEFAULT_ERROR_MESSAGE;
        this.regex = regex;
    }

    @Override
    public boolean validate() {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(getValue());
        if (!matcher.matches()) {
            return false;
        }

        return true;
    }
}

