package com.mind.data.di.interceptor

import com.google.gson.Gson
import com.jeremyliao.liveeventbus.LiveEventBus
import com.mind.data.data.mmkv.KV
import com.mind.data.event.MsgEvent.TOKEN_OUT
import com.mind.lib.data.model.Res
import com.mind.lib.util.CacheManager
import com.tencent.mmkv.MMKV
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody


class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        return if (response.body != null) {
            val body = response.body?.string()
            try {
                val model = Gson().fromJson(body, Res::class.java)
                if (model != null && model.code == 401) {
                    // 登录失效
                    MMKV.defaultMMKV().putString(KV.USER_INFO, "")
                    CacheManager.instance.clearLogin()
                    LiveEventBus.get(TOKEN_OUT).post(model.message)
                }
                val responseBody = body?.toResponseBody()
                response.newBuilder().body(responseBody).build()
            } catch (e: Exception) {
                e.printStackTrace()
                response
            }

        } else {
            response
        }

    }
}