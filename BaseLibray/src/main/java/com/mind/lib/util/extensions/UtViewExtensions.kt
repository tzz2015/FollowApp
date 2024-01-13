package com.mind.lib.util.extensions

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Outline
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import java.util.*

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visibleOrGone(isVisible: Boolean) {
    if (isVisible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun View.visibleOrInvisible(isVisible: Boolean) {
    if (isVisible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.INVISIBLE
    }
}

fun View.isVisible() = this.visibility == View.VISIBLE

fun View.isGone() = this.visibility == View.GONE

val View.marginParams: ViewGroup.MarginLayoutParams
    get() = (this.layoutParams as ViewGroup.MarginLayoutParams)

/**
 * 给 View 设置圆角
 */
fun View.setRadius(radius: Number) {
    outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, radius.toFloat())
        }
    }
    clipToOutline = true
}

fun View.setCircular() {
    clipToOutline = true
    outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            val centerX = (view.width / 2).toFloat()
            val centerY = (view.height / 2).toFloat()
            val radius = Math.min(view.width, view.height) / 2f
            outline.setOval(
                ((centerX - radius).toInt()),
                ((centerY - radius).toInt()),
                ((centerX + radius).toInt()),
                ((centerY + radius).toInt())
            )
        }
    }
}


fun ImageView.tintColor(color: Int) {
    this.setColorFilter(color)
}

fun Drawable.tintColor(color: Int): Drawable {
    val ret = DrawableCompat.wrap(this)
    DrawableCompat.setTintList(ret, ColorStateList.valueOf(color))
    return ret
}

fun Bitmap.tintColor(color: Int): Bitmap {
    val outBitmap = Bitmap.createBitmap(this.width, this.height, this.config)
    val canvas = android.graphics.Canvas(outBitmap)
    val paint = android.graphics.Paint()
    paint.colorFilter =
        android.graphics.PorterDuffColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, 0f, 0f, paint)

    return outBitmap
}


fun TextView.setColor(resId: Int) {
    this.setTextColor(ContextCompat.getColor(this.context, resId))
}

fun TextView.upperFirstLetter() {
    var text = this.text.toString()
    if (TextUtils.isEmpty(text)) {
        return
    }
    try {
        text =
            text.substring(0, 1).uppercase(this.textLocale) + text.substring(1, text.length)
                .lowercase(
                    Locale.getDefault()
                )
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    this.text = text
}

fun TextView.upperEachFirstLetter() {
    var text = this.text.toString()
    if (TextUtils.isEmpty(text)) {
        return
    }
    try {
        text = text.trim { it <= ' ' }
        val splitArray = text.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val textBuilder = StringBuilder()
        for (i in splitArray.indices) {
            val s = splitArray[i]
            if (!TextUtils.isEmpty(s)) {
                if (s.length > 1) {
                    textBuilder.append(
                        s.substring(0, 1).uppercase(textLocale)
                    )
                        .append(s.substring(1).lowercase(Locale.getDefault()))
                } else {
                    textBuilder.append(s)
                }
                if (i != splitArray.size - 1) {
                    textBuilder.append("\t")
                }
            }
        }
        this.text = textBuilder.toString()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun TextView.uppercase() {
    val text = text.toString()
    if (TextUtils.isEmpty(text)) {
        return
    }
    this.text = text.uppercase(this.textLocale)
}

fun TextView.lowercase() {
    val text = text.toString()
    if (TextUtils.isEmpty(text)) {
        return
    }
    this.text = text.lowercase(this.textLocale)
}

fun TextView.setResText(resId: Int) {
    this.text = resources.getText(resId)
}

fun ImageView.setColorFilter(resId: Int) {
    this.setColorFilter(ContextCompat.getColor(this.context, resId))
}

fun setOnClick(vararg views: View?, onClick: (View) -> Unit) {
    views.forEach {
        it?.setOnClickListener() { view ->
            onClick.invoke(view)
        }
    }
}

