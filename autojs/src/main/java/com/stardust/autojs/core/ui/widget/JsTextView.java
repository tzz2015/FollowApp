package com.stardust.autojs.core.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

/**
 * Created by Stardust on 2017/5/15.
 */
@Keep
@SuppressLint("AppCompatCustomView")
public class JsTextView extends TextView {

    public JsTextView(Context context) {
        super(context);
    }

    public JsTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public JsTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String text() {
        return getText().toString();
    }

    public void text(CharSequence text) {
        setText(text);
    }
}
