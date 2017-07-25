package ${packageName};

import android.support.design.widget.TextInputLayout;
import android.view.View;

import com.validator.*;
import com.validator.bind.IBindValidator;

import java.util.ArrayList;
import java.util.Map;

public final class ${parentClassName}_BindValidator<T extends ${parentClassName}> implements IBindValidator {

    private T view;
    private ArrayList<IFieldWrapper> fields = new ArrayList<>();
    private ArrayList<Integer> inactiveFieldIds = new ArrayList<>();

    public ${parentClassName}_BindValidator(Object target) {
        view = (T) target;
        #foreach($field in $fields)

        fields.add(new FieldWrapper(new ${field.type}(view.${field.name}.getId()#if($field.pattern),"$field.pattern"#end)#if($field.errorMessage){
            {
                    #if (${field.type.equals("PasswordValidator")})
                    defaultErrorMessage = "$field.errorMessage";
                    #else
                    errorMessage = "$field.errorMessage";
                    #end
            }
            }#end) {
            private final TextInputLayout layout = searchLayout(view.${field.name}, 3);

            @Override
            public Object getValue() {
                if (plugin != null)
                    return plugin.getValue();
                #if (${field.type.equals("PasswordValidator")})

                return new String[]{view.${field.name}.getText().toString()#if (${field.related}),view.${field.related}.getText().toString()#end};
                #elseif (${field.type.equals("CheckValidator")})

                return view.${field.name}.isChecked();
                #else

                return view.${field.name}.getText().toString();
                #end

            }

            @Override
            public void setError(String message) {
                if (plugin != null && plugin.setError(message))
                    return;

                if (layout == null)
                    view.${field.name}.setError(message);
                else
                    layout.setError(message);
            }

            @Override
            public boolean isActiveValidator() {
                return (layout != null && layout.getVisibility() == 0)
                        && view.${field.name}.getVisibility() == 0
                        && !inactiveFieldIds.contains(getValidator().getId());

            }

            #if ($field.field)
            @Override
            public String getField() {
                return "${field.field}";

            }#end

        });
        #end

    }

    private TextInputLayout searchLayout(View view, int depth) {
        if (view instanceof TextInputLayout)
            return (TextInputLayout) view;
        else if (depth > 0) {
            depth--;
            return searchLayout((View) view.getParent(), depth);
        }

        return null;
    }

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
    public IFieldWrapper getFieldWrapper(String field) {
        for (IFieldWrapper wrapper : fields) {
            if (wrapper.getField() != null && wrapper.getField().equals(field))
                return wrapper;
        }

        return null;
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