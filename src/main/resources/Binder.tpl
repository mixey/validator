package ${packageName};

import android.support.design.widget.TextInputLayout;
import android.view.View;

import com.validator.*;
import com.validator.bind.IBindValidator;
import com.validator.bind.BindValidator;

import java.util.ArrayList;


public final class ${parentClassName}_BindValidator<T extends ${parentClassName}> extends BindValidator implements IBindValidator {

    private T view;

    public ${parentClassName}_BindValidator(Object target) {
        view = (T) target;
        #foreach($field in $fields)

        fields.add(new FieldWrapper(new ${field.type}(view.getView().findViewById(${field.view}).getId()#if($field.pattern),"$field.pattern"#end)#if($field.errorMessage){
            {
                    #if (${field.type.equals("PasswordValidator")})
                    defaultErrorMessage = view.getString($field.errorMessage);
                    #else
                    errorMessage = view.getString($field.errorMessage);
                    #end
            }
            }#end) {
            private final $field.className fieldView = ($field.className) view.getView().findViewById(${field.view});
            private final TextInputLayout layout = searchLayout(fieldView, 3);

            @Override
            public Object getValue() {
                if (plugin != null)
                    return plugin.getValue();
                #if (${field.type.equals("PasswordValidator")})

                return new String[]{fieldView.getText().toString()#if (${field.related}),(($field.className) view.getView().findViewById(${field.related})).getText().toString()#end};
                #elseif (${field.type.equals("CheckValidator")})

                return fieldView.isChecked();
                #else

                return fieldView.getText().toString();
                #end

            }

            @Override
            public void setError(String message) {
                if (plugin != null && plugin.setError(message))
                    return;

                if (layout == null){
                    fieldView.setError(message);
                } else{
                    layout.setError(message);
                    layout.setErrorEnabled(message != null);
                }
            }

            @Override
            public boolean isActiveValidator() {
                return (layout != null && layout.getVisibility() == 0)
                        && fieldView.getVisibility() == 0
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
}