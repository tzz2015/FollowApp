package com.stardust.autojs.core.ui.attribute;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.Keep;

import com.stardust.autojs.core.ui.inflater.ResourceParser;
@Keep
public class TextViewAttributes extends ViewAttributes {

    public TextViewAttributes(ResourceParser resourceParser, View view) {
        super(resourceParser, view);
    }

    @Override
    protected void onRegisterAttrs() {
        super.onRegisterAttrs();
    }

    @Override
    public TextView getView() {
        return (TextView) super.getView();
    }
}
