package com.stardust.concurrent;

import androidx.annotation.Keep;

/**
 * Created by Stardust on 2017/12/27.
 */
@Keep

public class Value<T> {

    private T mValue;

    public Value(T value) {
        mValue = value;
    }

    public Value() {
    }

    public T get() {
        return mValue;
    }

    public void set(T value) {
        mValue = value;
    }


}
