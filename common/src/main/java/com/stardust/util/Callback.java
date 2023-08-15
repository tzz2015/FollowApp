package com.stardust.util;

import androidx.annotation.Keep;

/**
 * Created by Stardust on 2017/4/18.
 */
@Keep

public interface Callback<T> {

    void call(T t);
}
