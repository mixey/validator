package com.validator.bind;

import com.validator.IFieldWrapper;
import com.validator.IValidator;
import com.validator.ValidatorGroup;

import java.util.ArrayList;
import java.util.Map;


public class BindValidator implements IBindValidator {
    protected ArrayList<IFieldWrapper> fields = new ArrayList<>();
    protected ArrayList<Integer> inactiveFieldIds = new ArrayList<>();

    public void add(IFieldWrapper field) {
        fields.add(field);
    }

    @Override
    public IFieldWrapper getFieldWrapper(int fieldId) {
        for (IFieldWrapper wrapper : fields) {
            if (wrapper.getValidator().getId() == fieldId)
                return wrapper;
        }

        return null;
    }

    @Override
    public int getWidgetId(String field) {
        for (IFieldWrapper wrapper : fields) {
            if (wrapper.getField() != null && wrapper.getField().equals(field))
                return wrapper.getValidator().getId();
        }

        return -1;
    }


    public ValidatorGroup getValidator() {
        ValidatorGroup validator = new ValidatorGroup();
        for (IFieldWrapper field : fields) {
            field.setError(null);
            if (!field.isActiveValidator()) continue;
            IValidator v = field.getValidator();
            v.setValue(field.getValue());
            validator.add(v);
        }

        return validator;
    }

    public void setErrors(Map<String, String> errors) {
        for (Map.Entry<String, String> entry : errors.entrySet()) {
            for (IFieldWrapper field : fields) {
                if (String.valueOf(field.getValidator().getId()).equals(entry.getKey())
                        || field.getField() != null && field.getField().equals(entry.getKey())) {
                    field.setError(entry.getValue());
                    break;
                }
            }
        }
    }

    public void enableAll() {
        inactiveFieldIds.clear();
    }

    public void disable(int[] ids) {
        for (int id : ids) {
            inactiveFieldIds.add(id);
        }
    }
}
