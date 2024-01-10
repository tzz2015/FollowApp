package com.stardust.util.extensions

import android.os.Bundle

fun Bundle.getIntOrNull(key: String): Int? {
    return if (containsKey(key)) getInt(key) else null
}

fun Bundle.getLongOrNull(key: String): Long? {
    return if (containsKey(key)) getLong(key) else null
}

fun Bundle.getFloatOrNull(key: String): Float? {
    return if (containsKey(key)) getFloat(key) else null
}

fun Bundle.getDoubleOrNull(key: String): Double? {
    return if (containsKey(key)) getDouble(key) else null
}

fun Bundle.getBooleanOrNull(key: String): Boolean? {
    return if (containsKey(key)) getBoolean(key) else null
}

fun Bundle.getStringOrNull(key: String): String? {
    return if (containsKey(key)) getString(key) else null
}

fun Bundle.getBundleOrNull(key: String): Bundle? {
    return if (containsKey(key)) getBundle(key) else null
}
