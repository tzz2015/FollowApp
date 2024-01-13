package com.lyflovelyy.followhelper.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.linsh.utilseverywhere.LogUtils
import com.linsh.utilseverywhere.StringUtils
import com.lyflovelyy.followhelper.App
import com.lyflovelyy.followhelper.R
import com.lyflovelyy.followhelper.activity.GuideActivity
import com.lyflovelyy.followhelper.activity.LoginActivity
import com.lyflovelyy.followhelper.activity.UpdateInfoActivity
import com.lyflovelyy.followhelper.entity.BundleKeys
import com.lyflovelyy.followhelper.entity.Constants
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
    private var randomFollowCount: Int = 50
    private var randomUserCount: Int = 10
    private val mContext: Context by lazy { App.getContext() }


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
                            .getLong(
                                KV.LAST_USER_COUNT,
                                it
                            ) + (Math.random() * randomUserCount).toLong()
                    randomUserCount = 0
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
                            .getLong(
                                KV.LAST_FOLLOW_COUNT,
                                it
                            ) + (Math.random() * randomFollowCount).toLong()
                    var count = max(it, lastCount)
                    randomFollowCount = 0
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

    /**
     * 绑定抖音号或者修改抖音号
     */
    fun toBindAccount() {
        if (!isLogined()) {
            toLogin()
            return
        }
        if (StringUtils.isAllNotEmpty(CacheManager.instance.getDYAccount())) {
            toUpdateActivity(mContext.getString(R.string.modify_bind_account))
        } else {
            toUpdateActivity(mContext.getString(R.string.add_bind_account))
        }
    }

    private fun toUpdateActivity(name: String) {
        val intent = Intent(mContext, UpdateInfoActivity::class.java)
        if (mContext !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        intent.putExtra(Constants.UPDATE_FUNCTION, name)
        mContext.startActivity(intent)
    }

    private fun toLogin() {
        LogUtils.e("点击登录")
        val intent = Intent(mContext, LoginActivity::class.java)
        if (mContext !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        mContext.startActivity(intent)
    }

    fun toFollow() {
        if (!isLogined()) {
            toLogin()
            return
        }
        val intent = Intent(mContext, GuideActivity::class.java)
        if (mContext !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        intent.putExtra(BundleKeys.GUIDE_TYPE, 0)
        mContext.startActivity(intent)
    }


}