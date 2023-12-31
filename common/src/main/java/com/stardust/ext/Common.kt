package com.stardust.ext

import androidx.annotation.Keep
import kotlin.reflect.KMutableProperty0
@Keep

fun <R, T : KMutableProperty0<R?>> T.ifNull(provider: () -> R): R {
    val value = this.get()
    if (value != null) {
        return value
    }
    val newValue = provider()
    set(newValue)
    return newValue
}