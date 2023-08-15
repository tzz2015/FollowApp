package com.stardust.util;

import androidx.annotation.Keep;

/**
 * Created by Stardust on 2017/7/7.
 */
@Keep

public interface Func1<T, R> {

    R call(T t);

}
