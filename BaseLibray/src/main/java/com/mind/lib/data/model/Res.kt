package com.mind.lib.data.model

import androidx.annotation.Keep


/**
 * 混淆的时候 注意加上！！！！！！！！！！！！！！！
 */
@Keep
data class Res<T>(
    var code: Int = 0,
    var message: String = "",
    val data: T?
)