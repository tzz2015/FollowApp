package com.mind.lib.util.extensions

import android.graphics.RectF
import java.util.*
import kotlin.math.absoluteValue


/**
 * 对一个 Result 的 成功状态时的返回值 进行转换，返回一个 [transform] 转换后的 Result
 */
inline fun <R, T> Result<T>.merge(transform: (value: T) -> Result<R>): Result<R> {
    return try {
        fold({
            transform(it)
        }, {
            Result.failure(it)
        })
    } catch (ex: Throwable) {
        Result.failure(ex)
    }

}

inline fun Boolean?.ifTrue(block: () -> Unit): Boolean? {
    if (this == true) {
        block()
    }
    return this
}

inline fun Boolean?.ifFalse(block: () -> Unit): Boolean? {
    if (this == false) {
        block()
    }
    return this
}

/**
 * 根据[digits]返回指定小数点位数的字符串。会四舍五入
 */
fun Double.format(digits: Int) = "%.${digits}f".format(Locale.ENGLISH,this)

/**
 * 根据[digits]返回补零后的字符串，例如 1.format(2) 返回 01
 */
fun Int.format(digits: Int) = "%0${digits}d".format(Locale.ENGLISH,this)

/**
 * 返回一个对象的类名与地址，类似于 Object.toString() 的效果。
 * 为了解决某些重写了 toString 方法的类不方便区分是不是同一个对象。
 */
fun Any.idPrint() : String = "${this.javaClass.simpleName}@${System.identityHashCode(this)}"

/**
 * 模糊比较，两个浮点数接近一定的程度即认为相等
 * 更多方案可以参考：<https://levelup.gitconnected.com/double-equality-in-kotlin-f99392cba0e4>
 */
fun Float.equalsDelta(other: Float): Boolean {
    return if (other != 0f) {
        Math.abs(this / other - 1) < 0.000001 * Math.max(Math.abs(this), Math.abs(other))
    } else {
        (this - other).absoluteValue < 0.000001
    }
}

/**
 * 模糊比较，两个浮点数接近一定的程度即认为相等
 * 更多方案可以参考：<https://levelup.gitconnected.com/double-equality-in-kotlin-f99392cba0e4>
 */
fun Double.equalsDelta(other: Double): Boolean {
    return if (other != 0.0) {
        Math.abs(this / other - 1) < 0.000001 * Math.max(Math.abs(this), Math.abs(other))
    } else {
        (this - other).absoluteValue < 0.000001
    }
}

/**
 * 根据 Rect 的中心点进行缩放[scale]倍
 */
fun RectF.scale(scale: Float): RectF {
    val centerX = (left + right) / 2
    val centerY = (top + bottom) / 2
    val width = right - left
    val height = bottom - top
    val newWidth = width * scale
    val newHeight = height * scale
    return RectF(
        centerX - newWidth / 2,
        centerY - newHeight / 2,
        centerX + newWidth / 2,
        centerY + newHeight / 2
    )
}