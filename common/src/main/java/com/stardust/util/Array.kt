package com.stardust.util

import androidx.annotation.Keep
import java.util.*
@Keep

inline fun <reified T> sortedArrayOf(vararg elements: T): Array<T> {
    val a = arrayOf(*elements)
    Arrays.sort(a)
    return a
}