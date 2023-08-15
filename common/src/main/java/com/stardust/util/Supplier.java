package com.stardust.util;

import androidx.annotation.Keep;

/**
 * Created by Stardust on 2017/5/1.
 */
@Keep

public interface Supplier<T> {
    T get();
}
