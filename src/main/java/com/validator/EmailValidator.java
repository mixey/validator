package com.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator extends BaseValidator<String> {
    private final String PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private final String DEFAULT_ERROR_MESSAGE = "Неправильный формат Email";

    public EmailValidator(int id) {
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

