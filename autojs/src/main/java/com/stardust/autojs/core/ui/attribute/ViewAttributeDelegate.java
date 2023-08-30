package com.stardust.autojs.core.ui.attribute;

import android.view.View;

import androidx.annotation.Keep;

@Keep
public interface ViewAttributeDelegate {
    @Keep
    interface ViewAttributeGetter {
        String get(String name);
    }
    @Keep
    interface ViewAttributeSetter {
        void set(String name, String value);
    }

    boolean has(String name);

    String get(View view, String name, ViewAttributeGetter defaultGetter);

    void set(View view, String name, String value, ViewAttributeSetter defaultSetter);

}
