package com.stardust.util;

import androidx.annotation.Keep;

/**
 * Created by Stardust on 2017/11/26.
 */
@Keep

public class Nath {

    public static int min(int... ints) {
        int min = ints[0];
        for (int i = 1; i < ints.length; i++) {
            min = ints[i] < min ? ints[i] : min;
        }
        return min;
    }
}
