package com.stardust.autojs.core.pref

import androidx.annotation.Keep
import androidx.preference.PreferenceManager;
import com.stardust.app.GlobalAppContext
@Keep
object Pref {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(GlobalAppContext.get())
    val isStableModeEnabled: Boolean
        get() {
            return preferences.getBoolean("key_stable_mode", false)
        }

    val isGestureObservingEnabled: Boolean
        get() {
            return preferences.getBoolean("key_gesture_observing", false)
        }
}