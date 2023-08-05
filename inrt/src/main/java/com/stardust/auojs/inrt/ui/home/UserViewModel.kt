package com.stardust.auojs.inrt.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.linsh.utilseverywhere.LogUtils
import com.linsh.utilseverywhere.ToastUtils
import com.mind.data.data.model.FollowAccount
import com.mind.data.data.model.FollowAccountType
import com.mind.data.data.model.UserModel
import com.mind.data.http.ApiClient
import com.mind.lib.base.BaseViewModel
import com.mind.lib.base.ViewModelEvent
import com.stardust.app.GlobalAppContext
import com.stardust.auojs.inrt.data.Constants
import com.stardust.auojs.inrt.ui.mine.RegisterActivity
import com.stardust.auojs.inrt.util.isLogined
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

/**
 * @Author      : liuyufei
 * @Date        : on 2023-07-22 16:44.
 * @Description :描述
 */
@HiltViewModel
class UserViewModel @Inject constructor() : BaseViewModel() {
    val userCount by lazy { ViewModelEvent<String>() }
    val followAccount by lazy { ViewModelEvent<FollowAccount>() }

    //是否显示密码  默认不显示
    val isClose = MutableLiveData<Boolean>().apply { this.value = true }

    //账号
    val phone = MutableLiveData<String>()

    //密码
    val password = MutableLiveData<String>()
     // 邮箱
    val email = MutableLiveData<String>()


    val loginResult by lazy { MutableLiveData<UserModel>() }

    private val mContext: Context by lazy { GlobalAppContext.get() }

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

    fun getGifUrl(): String {
        val randomInt = Random.nextInt(0, Constants.GIF_ARRAY.size)
        return Constants.GIF_ARRAY[randomInt]
    }

    /**
     * 是否显示密码
     */
    fun showPwd() {
        val value = isClose.value ?: false
        isClose.postValue(!value)
    }

    fun login() {
        if (phone.value.isNullOrEmpty()) {
            ToastUtils.show("账号不能为空")
            return
        }
        if (password.value.isNullOrEmpty()) {
            ToastUtils.show("密码不能为空")
            return
        }
        val map = hashMapOf(
            "phone" to (phone.value ?: ""),
            "password" to (password.value ?: "")
        )
        loadHttp(
            request = { ApiClient.userApi.login(map) },
            resp = {
                ToastUtils.show("登录成功")
                LogUtils.e(it.toString())
                loginResult.postValue(it)
            },
            isShowDialog = true
        )
    }

    fun toRegister() {
        val intent = Intent(mContext, RegisterActivity::class.java)
        if (mContext !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        mContext.startActivity(intent)
    }

}