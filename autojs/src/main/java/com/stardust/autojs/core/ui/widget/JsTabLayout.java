package com.stardust.autojs.core.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Keep;

import com.google.android.material.tabs.TabLayout;
@Keep
public class JsTabLayout extends TabLayout {

    public JsTabLayout(Context context) {
        super(context);
    }

    public JsTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JsTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
