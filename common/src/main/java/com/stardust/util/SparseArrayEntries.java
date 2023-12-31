package com.stardust.util;

import android.util.SparseArray;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

/**
 * Created by Stardust on 2017/1/26.
 */
@Keep

public class SparseArrayEntries<E> {

    private final SparseArray<E> mSparseArray = new SparseArray<>();

    public SparseArrayEntries<E> entry(int key, E value) {
        mSparseArray.put(key, value);
        return this;
    }

    @NonNull
    public SparseArray<E> sparseArray() {
        return mSparseArray;
    }

}