package com.stardust.auojs.inrt.ui.home

import android.util.Log
import com.mind.data.http.ApiClient
import com.mind.lib.base.BaseViewModel
import javax.inject.Inject

/**
 * @Author      : liuyufei
 * @Date        : on 2023-07-22 16:44.
 * @Description :描述
 */
class UserViewModel @Inject constructor() : BaseViewModel() {
    /**
     * 获取用户总数量
     */
    fun getUserCount() {
        loadHttp(
            request = { ApiClient.userApi.getTotalUserCount() },
            resp = { Log.e(javaClass.name, "getUserCount:${it} ") },
        )

    }
}