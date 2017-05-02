package com.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ValidatorGroup {
    private ArrayList<IValidator> validators = new ArrayList<>();
    private Map<String, String> errors;

    public void add(IValidator validator) {
        validators.add(validator);
    }

    public boolean validate() {
        boolean result = true;
        errors = new HashMap<>();
        for (IValidator v : validators) {
            if (!v.validate()) {
                errors.put(String.valueOf(v.getId()), v.getErrorMessage());
                result = false;
            }
        }

        return result;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public ArrayList<IValidator> getValidators() {
        return validators;
    }
}
