package com.lyflovelyy.followhelper.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import com.lyflovelyy.followhelper.App
import com.lyflovelyy.followhelper.activity.LoginActivity
import com.mind.data.data.mmkv.KV
import com.mind.data.data.model.AppType
import com.mind.data.data.model.ScriptType
import com.mind.lib.util.CacheManager
import com.tencent.mmkv.MMKV

/**
 *Created by Rui
 *on 2020/12/31
 */

/**
 * 是否登录
 */
fun isLogined(): Boolean {
    return CacheManager.instance.getToken().isNotEmpty()
}

/**
 * 是否登录
 * 如果登录执行method()
 */
fun afterLogin(method: () -> Unit) {
    if (isLogined()) {
        method()
    } else {
        val context = App.getContext()
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}

fun copyToClipboard(text: String) {
    try {
        // 获取剪切板管理器实例
        val clipboardManager: ClipboardManager =
            App.getContext()
                .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 要复制的文本
        // 创建一个 ClipData 对象，包含要复制的文本
        val clipData: ClipData = ClipData.newPlainText("label", text)
        // 将 ClipData 对象放入剪切板
        clipboardManager.setPrimaryClip(clipData)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private var lastClickTime = 0L
private const val CLICK_INTERVAL = 1000 // 1 second
fun afterSafeOnClick(method: () -> Unit) {
    val currentTime = SystemClock.elapsedRealtime()
    if (currentTime - lastClickTime >= CLICK_INTERVAL) {
        lastClickTime = currentTime
        method()
    }
}

/**
 * 关注类型
 */
fun getFollowType(): Int {
    val appType = MMKV.defaultMMKV().getInt(KV.APP_TYPE, 0)
    if (appType == AppType.DOU_YIN) {
        return ScriptType.DOU_YIN
    } else if (appType == AppType.TIKTOP) {
        return ScriptType.TIK_TOP
    }
    return ScriptType.DOU_YIN
}

/**
 * 点赞类型
 */
fun getPraiseType(): Int {
    val appType = MMKV.defaultMMKV().getInt(KV.APP_TYPE, 0)
    if (appType == AppType.DOU_YIN) {
        return ScriptType.DOU_YIN_PRAISE
    } else if (appType == AppType.TIKTOP) {
        return ScriptType.TIK_TOP_PRAISE
    }
    return ScriptType.DOU_YIN_PRAISE
}

/**
 * 刷评论
 */
fun getCommentType(): Int {
    val appType = MMKV.defaultMMKV().getInt(KV.APP_TYPE, 0)
    if (appType == AppType.DOU_YIN) {
        return ScriptType.DOUYIN_COMMENT
    } else if (appType == AppType.TIKTOP) {
        return ScriptType.TIK_TOP_COMMENT
    }
    return ScriptType.DOUYIN_COMMENT
}


fun isZh(): Boolean {
    val locale = App.getContext().resources.configuration.locale
    val language = locale.language
    return language.contains("zh")
}


fun formatLargeNumber(number: Long): String {
    return if (number < 1000) {
        number.toString() // 数字小于1000，不需要后缀
    } else if (number < 10000) {
        // 数字在1000和9999之间，将其转换成x.xk的格式
        val thousands = number / 1000.0
        String.format("%.1fk", thousands)
    } else {
        // 数字大于等于10000，将其转换成x.xw的格式
        val millions = number / 10000.0
        String.format("%.1fw", millions)
    }
}


