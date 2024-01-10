package com.stardust.util.extensions

import android.content.res.Resources


/**
 * dp to px
 */
val Number.dpToPx: Int
    get() = (this.toFloat() * Resources.getSystem().displayMetrics.density).toInt()

/**
 * dp to px
 */
val Number.dp: Int
    get() = (this.toFloat() * Resources.getSystem().displayMetrics.density).toInt()

/**
 * dp to px
 */
val Number.pxToDp: Int
    get() = (this.toFloat() / Resources.getSystem().displayMetrics.density).toInt()

