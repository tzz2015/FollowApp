package com.stardust.autojs.core.looper;

import android.os.Looper;

import androidx.annotation.Keep;

import java.util.concurrent.ConcurrentHashMap;
@Keep
public class LooperHelper {

    private static volatile ConcurrentHashMap<Thread, Looper> sLoopers = new ConcurrentHashMap<>();

    public static void prepare() {
        if (Looper.myLooper() == Looper.getMainLooper())
            return;
        if (Looper.myLooper() == null)
            Looper.prepare();
        Looper l = Looper.myLooper();
        if (l != null)
            sLoopers.put(Thread.currentThread(), l);
    }

    public static void quitForThread(Thread thread) {
        Looper looper = sLoopers.remove(thread);
        if (looper != null && looper != Looper.getMainLooper())
            looper.quit();
    }
}
