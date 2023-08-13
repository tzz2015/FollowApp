package com.mind.lib.util

import androidx.annotation.Keep

@Keep
class CacheManager private constructor() {

    companion object {
        private const val TOKEN = "token"
        private const val PHONE = "phone"
        private const val EMAIL = "email"
        private const val DOUYIN_ACCOUNT = "douyin_account"
        private const val VERSION = "version"



        val instance: CacheManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CacheManager()
        }
    }

    val map = HashMap<String, String>()

    /**
     * 存入
     */
    fun put(
        key: String, value: String
    ) {
        this.map[key] = value
    }

    /**
     * 取数据
     */
    fun get(key: String): String? {
        return map[key]
    }

    /**
     * 放入token
     */
    fun putToken(value: String?) {
        value?.let {
            map[TOKEN] = value
        }
    }

    /**
     * 取出Token
     */
    fun getToken() = map[TOKEN] ?: ""

    /**
     * 放入版本号
     */
    fun putVersion(version: String) {
        map[VERSION] = version
    }

    /**
     * 取出版本号
     */
    fun getVersion() = map[VERSION] ?: ""

    fun putPhone(value: String?) {
        value?.let {
            map[PHONE] = value
        }
    }

    fun getPhone() = map[PHONE] ?: ""

    fun putEmail(value: String?) {
        value?.let {
            map[EMAIL] = value
        }
    }

    fun getEmail() = map[EMAIL] ?: ""

    /**
     * 放入token
     */
    fun putDYAccount(value: String?) {
        value?.let {
            map[DOUYIN_ACCOUNT] = value
        }
    }

    /**
     * 取出Token
     */
    fun getDYAccount() = map[DOUYIN_ACCOUNT] ?: ""


    fun clearLogin() {
        map.remove(PHONE)
        map.remove(EMAIL)
        map.remove(TOKEN)
        map.remove(DOUYIN_ACCOUNT)
    }

}