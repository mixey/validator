package com.validator;

import android.support.annotation.Nullable;

public interface IFieldPlugin {
    Object getValue();

    boolean setError(@Nullable String message);
}
