package com.lyflovelyy.followhelper.viewmodel

import android.util.Log
import com.linsh.utilseverywhere.StringUtils
import com.lyflovelyy.followhelper.utils.formatLargeNumber
import com.lyflovelyy.followhelper.utils.getFollowType
import com.lyflovelyy.followhelper.utils.isLogined
import com.mind.data.data.mmkv.KV
import com.mind.data.data.model.FollowAccount
import com.mind.data.http.ApiClient
import com.mind.lib.base.BaseViewModel
import com.mind.lib.base.ViewModelEvent
import com.mind.lib.util.CacheManager
import com.tencent.mmkv.MMKV
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Long.max
import javax.inject.Inject

/**
 * @Author      : liuyufei
 * @Date        : on 2024-01-02 21:36.
 * @Description :
 */
@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel() {
    val userCount by lazy { ViewModelEvent<String>() }
    val followAccount by lazy { ViewModelEvent<FollowAccount>() }
    val followCount by lazy { ViewModelEvent<String>() }

    /**
     * 获取用户总数量
     */
    fun getTotalUserCount() {
        loadHttp(
            request = { ApiClient.userApi.getTotalUserCount() },
            resp = {
                it?.let {
                    val lastCount =
                        MMKV.defaultMMKV()
                            .getLong(KV.LAST_USER_COUNT, it) + (Math.random() * 10).toLong()
                    var count = max(it, lastCount)
                    if (count < 500) {
                        count += 500
                    }
                    Log.e(javaClass.simpleName, "getUserCount:${count} ")
                    MMKV.defaultMMKV().putLong(KV.LAST_USER_COUNT, count)
                    userCount.postValue(formatLargeNumber(count))
                }

            },
            err = {
                userCount.postValue(formatLargeNumber(8897875898))
            },
            isShowDialog = false

        )

    }

    /**
     * 获取个人绑定账户
     */
    fun getFollowAccount(isFore: Boolean = false) {
        if (isLogined()) {
            val dyAccount = CacheManager.instance.getDYAccount()
            if (!isFore && StringUtils.isNotAllEmpty(dyAccount)) {
                return
            }
            loadHttp(
                request = { ApiClient.followAccountApi.getFollowAccount(getFollowType()) },
                resp = {
                    Log.e(javaClass.simpleName, "getFollowAccount:${it.toString()} ")
                    it?.let { followAccount.postValue(it) }
                    if (it == null) {
                        followAccount.postValue(FollowAccount())
                    }
                },
                isShowDialog = false
            )
        }

    }

    /**
     * 获取总关注数量
     */
    fun getTotalFollowCount() {
        loadHttp(
            request = { ApiClient.followApi.getTotalFollowCount() },
            resp = {
                it?.let {
                    val lastCount =
                        MMKV.defaultMMKV()
                            .getLong(KV.LAST_FOLLOW_COUNT, it) + (Math.random() * 50).toLong()
                    var count = max(it, lastCount)
                    if (count < 1000) {
                        count += 1000
                    }
                    Log.e(javaClass.simpleName, "getTotalFollowCount:${it} ")
                    MMKV.defaultMMKV().putLong(KV.LAST_FOLLOW_COUNT, count)
                    followCount.postValue(formatLargeNumber(count))
                }
            },
            err = {
                followCount.postValue(formatLargeNumber(4556762359))
            },
            isShowDialog = false
        )
    }

}