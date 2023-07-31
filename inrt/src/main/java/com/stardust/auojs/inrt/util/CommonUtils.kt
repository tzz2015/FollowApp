package com.stardust.auojs.inrt.util

import com.mind.lib.util.CacheManager

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
 * 如果没有登录执行 login()
 */
fun afterLogin(method: () -> Unit, login: () -> Unit) {
    if (isLogined()) {
        method()
    } else {
        login()
    }
}


