package android.widget;

import androidx.annotation.Keep;

import java.lang.reflect.Field;
@Keep
public class TextViewHelper {

    private static final Field sSavedStateText;

    static {
        Field text = null;
        try {
            text = TextView.SavedState.class.getDeclaredField("text");
            text.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        sSavedStateText = text;
    }

    public static void setText(TextView.SavedState state, CharSequence text) {
        if (sSavedStateText == null) {
            return;
        }
        try {
            sSavedStateText.set(state, text);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
