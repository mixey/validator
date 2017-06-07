package com.validator;

public abstract class FieldWrapper implements IFieldWrapper {
    protected IFieldPlugin plugin;
    private final IValidator validator;

    public FieldWrapper(IValidator validator) {
        this.validator = validator;
    }

    @Override
    public IValidator getValidator() {
        return validator;
    }

    @Override
    public boolean isActiveValidator() {
        return true;
    }

    @Override
    public String getField() {
        return null;
    }

    @Override
    public void addPlugin(IFieldPlugin plugin) {
        this.plugin = plugin;
    }
}
