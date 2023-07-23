package com.mind.data.http

import com.mind.data.data.api.ApiService
import com.mind.data.data.api.FollowApi
import com.mind.data.data.api.UserApi
import com.mind.data.http.RetrofitClient.retrofitClient

/**
 * create by Rui on 2022/4/14
 * desc: http api 初始化
 */
object ApiClient {


    // api 可自行扩展

    @Volatile
    @JvmStatic
    private var _USERAPI: UserApi? = null

    @Volatile
    @JvmStatic
    private var _FollowAPI: FollowApi? = null

    @Volatile
    @JvmStatic
    private var _OtherAPI: ApiService? = null


    @JvmStatic
    val followApi = _FollowAPI ?: synchronized(this) {
        _FollowAPI ?: retrofitClient.create(FollowApi::class.java).also { _FollowAPI = it }
    }

    @JvmStatic
    val userApi = _USERAPI ?: synchronized(this) {
        _USERAPI ?: retrofitClient.create(UserApi::class.java).also { _USERAPI = it }
    }

    @JvmStatic
    val otherApi = _OtherAPI ?: synchronized(this) {
        _OtherAPI ?: retrofitClient.create(ApiService::class.java).also { _OtherAPI = it }
    }
}