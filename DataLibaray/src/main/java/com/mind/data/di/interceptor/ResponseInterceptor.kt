package com.mind.data.di.interceptor

import com.google.gson.Gson
import com.jeremyliao.liveeventbus.LiveEventBus
import com.mind.data.event.MsgEvent.TOKEN_OUT
import com.mind.lib.data.model.Res
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody


class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        return if (response.body != null) {
            val body = response.body?.string()
            val model = Gson().fromJson(body, Res::class.java)
            if (model.code == 401) {
                LiveEventBus.get(TOKEN_OUT).post(model.message)
            }
            val responseBody = body?.toResponseBody()
            response.newBuilder().body(responseBody).build()
        } else {
            response
        }

    }
}