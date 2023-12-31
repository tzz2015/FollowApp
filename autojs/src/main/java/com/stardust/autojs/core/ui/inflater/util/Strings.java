package com.stardust.autojs.core.ui.inflater.util;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import androidx.annotation.Keep;

import com.stardust.app.GlobalAppContext;

/**
 * Created by Stardust on 2017/11/4.
 */
@Keep
public class Strings {


    public static String parse(Context context, String str) {
        if (str.startsWith("@string/")) {
            Resources resources = context.getResources();
            return resources.getString(resources.getIdentifier(str, "string",
                    GlobalAppContext.getAutojsPackageName()));
        }
        return str;
    }

    public static String parse(View view, String str) {
        return parse(view.getContext(), str);
    }
}
