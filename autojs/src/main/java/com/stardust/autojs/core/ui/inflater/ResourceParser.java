package com.stardust.autojs.core.ui.inflater;

import androidx.annotation.Keep;

import com.stardust.autojs.core.ui.inflater.util.Drawables;

/**
 * Created by Stardust on 2018/1/24.
 */
@Keep
public class ResourceParser {


    private final Drawables mDrawables;

    public ResourceParser(Drawables drawables) {
        mDrawables = drawables;
    }

    public Drawables getDrawables() {
        return mDrawables;
    }
}
