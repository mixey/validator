package ${packageName};

import android.support.design.widget.TextInputLayout;

import com.validator.*;
import com.validator.bind.IBindValidator;

import java.util.ArrayList;
import java.util.Map;

public final class ${parentClassName}_BindValidator<T extends ${parentClassName}> implements IBindValidator {

    private T view;
    private ArrayList<IFieldExtension> fields = new ArrayList<>();
    private ArrayList<Integer> inactiveFieldIds = new ArrayList<>();

    public ${parentClassName}_BindValidator(Object target) {
        view = (T) target;
        #foreach($field in $fields)

        fields.add(new IFieldExtension() {
            IValidator validator = new ${field.type}(view.${field.name}.getId());

            @Override
            public Object getValue() {
                #if (${field.type.equals("PasswordValidator")})

                String value2 = null;
                #if (!${field.related.isEmpty()})
                value2 = view.${field.related}.getText().toString();
                #end

                return new String[] {view.${field.name}.getText().toString(), value2};
                #elseif (${field.type.equals("CheckValidator")})

                return view.${field.name}.isChecked();
                #else

                return view.${field.name}.getText().toString();
                #end

            }
            @Override
            public IValidator getValidator() {
                return validator;
            }
            @Override
            public void setError(String message) {
                #if ($field.hasLayout)

                ((TextInputLayout) view.${field.name}.getParent().getParent()).setError(message);
                #else

                view.${field.name}.setError(message);
                #end

            }
            @Override
            public boolean isActiveValidator() {
                #if ($field.hasLayout)

                return ((TextInputLayout) view.${field.name}.getParent().getParent()).getVisibility() == 0
                    && !inactiveFieldIds.contains(getValidator().getId());
                #else

                return view.${field.name}.getVisibility() == 0
                    && !inactiveFieldIds.contains(getValidator().getId());
                #end

            }
            @Override
            public String getField() {
                #if ($field.field.isEmpty())

                return null;
                #else

                return "${field.field}";
                #end

            }
        });
        #end

    }

    public void add(IFieldExtension field) {
        fields.add(field);
    }

    public ValidatorGroup getValidator() {
        ValidatorGroup validator = new ValidatorGroup();
        for (IFieldExtension field : fields) {
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
            for (IFieldExtension field : fields) {
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