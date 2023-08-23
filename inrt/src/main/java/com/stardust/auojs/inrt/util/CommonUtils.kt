package com.stardust.auojs.inrt.util

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import com.mind.lib.util.CacheManager
import com.stardust.app.GlobalAppContext
import com.stardust.auojs.inrt.ui.mine.LoginActivity

/**
 *Created by Rui
 *on 2020/12/31
 */

/**
 * 是否登录
 */
fun isLogined(): Boolean {
    return  CacheManager.instance.getToken().isNotEmpty()
}

/**
 * 是否登录
 * 如果登录执行method()
 */
fun afterLogin(method: () -> Unit) {
    if (isLogined()) {
        method()
    } else {
        val context =GlobalAppContext.get()
        val intent = Intent(context, LoginActivity::class.java)
        if (context !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}
fun copyToClipboard(text: String) {
    try {
        // 获取剪切板管理器实例
        val clipboardManager: ClipboardManager =
            GlobalAppContext.get()
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


