package com.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator extends BaseValidator<String[]> {

    protected String defaultErrorMessage = "Некорректный пароль";

    private String regex = "[0-9a-zA-Z@#$%^&+=]{6,}";
    private final String DEFAULT_ERROR_MESSAGE2 = "Пароли не совпадают";

    public PasswordValidator(int id) {
        super(id);
    }

    public PasswordValidator(int id, String pattern) {
        this(id);
        this.regex = pattern;
    }

    @Override
    public boolean validate() {
        String[] values = getValue();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(values[0]);
        if (!matcher.matches()) {
            setErrorMessage(defaultErrorMessage);
            return false;
        }

        if (values.length > 1 && values[1] != null) {
            if (!values[1].equals(values[0])) {
                setErrorMessage(DEFAULT_ERROR_MESSAGE2);
                return false;
            }
        }

        return true;
    }
}
