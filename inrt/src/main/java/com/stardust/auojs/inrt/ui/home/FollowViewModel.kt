package com.stardust.auojs.inrt.ui.home

import android.util.Log
import com.mind.data.http.ApiClient
import com.mind.lib.base.BaseViewModel
import com.mind.lib.base.ViewModelEvent
import javax.inject.Inject

/**
 * @Author      : liuyufei
 * @Date        : on 2023-07-22 16:44.
 * @Description :描述
 */
class FollowViewModel @Inject constructor() : BaseViewModel() {
    val followCount by lazy { ViewModelEvent<String>() }

    /**
     * 获取总关注数量
     */
    fun getTotalFollowCount() {
        loadHttp(
            request = { ApiClient.followApi.getTotalFollowCount() },
            resp = {
                Log.e(javaClass.name, "getTotalFollowCount:${it} ")
                followCount.postValue("$it")
            },
            err = {
                followCount.postValue("10000")
            }
        )
    }
}