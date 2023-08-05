package com.stardust.auojs.inrt.ui.home

import android.util.Log
import com.mind.data.data.model.FollowAccount
import com.mind.data.data.model.FollowAccountType
import com.mind.data.http.ApiClient
import com.mind.lib.base.BaseViewModel
import com.mind.lib.base.ViewModelEvent
import com.stardust.auojs.inrt.util.isLogined
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @Author      : liuyufei
 * @Date        : on 2023-07-22 16:44.
 * @Description :描述
 */
@HiltViewModel
class UserViewModel @Inject constructor() : BaseViewModel() {
    val userCount by lazy { ViewModelEvent<String>() }
    val followAccount by lazy { ViewModelEvent<FollowAccount>() }

    /**
     * 获取用户总数量
     */
    fun getTotalUserCount() {
        loadHttp(
            request = { ApiClient.userApi.getTotalUserCount() },
            resp = {
                Log.e(javaClass.name, "getUserCount:${it} ")
                userCount.postValue("$it")
            },
            err = {
                userCount.postValue("1000")
            }

        )

    }

    /**
     * 获取个人绑定账户
     */
    fun getFollowAccount() {
        if (isLogined()) {
            loadHttp(
                request = { ApiClient.followAccountApi.getFollowAccount(FollowAccountType.DOU_YIN) },
                resp = {
                    Log.e(javaClass.name, "getFollowAccount:${it.toString()} ")
                    it?.let { followAccount.postValue(it) }
                },
                isShowDialog = false
            )
        }

    }

}