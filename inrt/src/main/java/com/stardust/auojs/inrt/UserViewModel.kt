package com.stardust.auojs.inrt

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.linsh.utilseverywhere.LogUtils
import com.linsh.utilseverywhere.StringUtils
import com.linsh.utilseverywhere.ToastUtils
import com.mind.data.data.mmkv.KV
import com.mind.data.data.model.AppType
import com.mind.data.data.model.FollowAccount
import com.mind.data.data.model.UserModel
import com.mind.data.http.ApiClient
import com.mind.lib.base.BaseViewModel
import com.mind.lib.base.ViewModelEvent
import com.mind.lib.util.CacheManager
import com.stardust.app.GlobalAppContext
import com.stardust.auojs.inrt.data.Constants
import com.stardust.auojs.inrt.ui.mine.*
import com.stardust.auojs.inrt.util.copyToClipboard
import com.stardust.auojs.inrt.util.formatLargeNumber
import com.stardust.auojs.inrt.util.getFollowType
import com.stardust.auojs.inrt.util.isLogined
import com.tencent.mmkv.MMKV
import dagger.hilt.android.lifecycle.HiltViewModel
import org.autojs.autoxjs.inrt.R
import javax.inject.Inject

/**
 * @Author      : liuyufei
 * @Date        : on 2023-07-22 16:44.
 * @Description :描述
 */
@HiltViewModel
open class UserViewModel @Inject constructor() : BaseViewModel() {
    val userCount by lazy { ViewModelEvent<String>() }
    val followAccount by lazy { ViewModelEvent<FollowAccount>() }

    /**
     * 注册成功
     */
    val isRegisterSuccess = MutableLiveData<Boolean>().apply { this.value = false }

    /**
     * 修改密码
     */
    val isChangePswSuccess = MutableLiveData<Boolean>().apply { this.value = false }


    //是否显示密码  默认不显示
    val isClose = MutableLiveData<Boolean>().apply { this.value = true }

    //手机号
    val phone = MutableLiveData<String>()

    //密码
    val password = MutableLiveData<String>()

    // 邮箱
    val email = MutableLiveData<String>()

    // 验证码
    val captchaCode = MutableLiveData<String>()


    val loginResult by lazy { MutableLiveData<UserModel>() }

    private val mContext: Context by lazy { GlobalAppContext.get() }

    fun init() {
        if (StringUtils.isAllNotEmpty(CacheManager.instance.getPhone())) {
            phone.postValue(CacheManager.instance.getPhone())
        }
        if (StringUtils.isAllNotEmpty(CacheManager.instance.getEmail())) {
            email.postValue(CacheManager.instance.getEmail())
        }
    }

    /**
     * 获取用户总数量
     */
    fun getTotalUserCount() {
        loadHttp(
            request = { ApiClient.userApi.getTotalUserCount() },
            resp = {
                it?.let {
                    Log.e(javaClass.simpleName, "getUserCount:${it} ")
                    userCount.postValue(formatLargeNumber(it))
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
     * 是否显示密码
     */
    fun showPwd() {
        val value = isClose.value ?: false
        isClose.postValue(!value)
    }

    fun login() {
        if (phone.value.isNullOrEmpty()) {
            ToastUtils.show(mContext.getString(org.autojs.autoxjs.inrt.R.string.username_not_empty))
            return
        }
        if (password.value.isNullOrEmpty()) {
            ToastUtils.show(mContext.getString(org.autojs.autoxjs.inrt.R.string.password_not_empty))
            return
        }
        val map = hashMapOf(
            "phone" to (phone.value ?: ""),
            "password" to (password.value ?: "")
        )
        loadHttp(
            request = { ApiClient.userApi.login(map) },
            resp = {
                ToastUtils.show(mContext.getString(org.autojs.autoxjs.inrt.R.string.login_success))
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

    fun toLogin() {
        LogUtils.e("点击登录")
        val intent = Intent(mContext, LoginActivity::class.java)
        if (mContext !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        mContext.startActivity(intent)
    }

    fun toChangePsw() {
        val intent = Intent(mContext, ChangePswActivity::class.java)
        if (mContext !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        mContext.startActivity(intent)
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

    fun toUpdateActivity(name: String) {
        val intent = Intent(mContext, UpdateInfoActivity::class.java)
        if (mContext !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        intent.putExtra(Constants.UPDATE_FUNCTION, name)
        mContext.startActivity(intent)
    }

    fun toSuggestionActivity() {
        val intent = Intent(mContext, SuggestionActivity::class.java)
        if (mContext !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        mContext.startActivity(intent)
    }

    fun toPrivacyPolicy() {
        val intent = Intent(mContext, PrivacyPolicyActivity::class.java)
        if (mContext !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        mContext.startActivity(intent)
    }



    fun register() {
        if (phone.value.isNullOrEmpty()) {
            ToastUtils.show(mContext.getString(R.string.username_not_empty))
            return
        }
        if (password.value.isNullOrEmpty()) {
            ToastUtils.show(mContext.getString(R.string.password_not_empty))
            return
        }
        if (email.value.isNullOrEmpty()) {
            ToastUtils.show(mContext.getString(R.string.email_not_empty))
            return
        }
        val map = hashMapOf(
            "phone" to (phone.value ?: ""),
            "password" to (password.value ?: ""),
            "email" to (email.value ?: "")
        )
        loadHttp(
            request = { ApiClient.userApi.register(map) },
            resp = {
                ToastUtils.show(mContext.getString(R.string.register_success))
                isRegisterSuccess.postValue(true)
            },
            isShowDialog = true
        )
    }

    /**
     * 发送验证码
     */
    fun sendCode(view: View) {
        if (phone.value.isNullOrEmpty()) {
            ToastUtils.show(mContext.getString(R.string.username_not_empty))
            return
        }
        if (email.value.isNullOrEmpty()) {
            ToastUtils.show(mContext.getString(R.string.email_not_empty))
            return
        }
        view.isEnabled = false
        val map = hashMapOf(
            "phone" to (phone.value ?: ""),
            "email" to (email.value ?: "")
        )
        loadHttp(
            request = { ApiClient.userApi.senCode(map) },
            resp = {
                ToastUtils.show(mContext.getString(R.string.send_code_success))
                view.isEnabled = true
            },
            err = {
                view.isEnabled = true
            },
            isShowDialog = true
        )

    }

    /**
     * 修改密码
     */
    fun changePsw() {
        if (phone.value.isNullOrEmpty()) {
            ToastUtils.show(mContext.getString(R.string.username_not_empty))
            return
        }
        if (password.value.isNullOrEmpty()) {
            ToastUtils.show(mContext.getString(R.string.password_not_empty))
            return
        }
        if (email.value.isNullOrEmpty()) {
            ToastUtils.show(mContext.getString(R.string.email_not_empty))
            return
        }
        val map = hashMapOf(
            "phone" to (phone.value ?: ""),
            "password" to (password.value ?: ""),
            "email" to (email.value ?: "")
        )
        loadHttp(
            request = { ApiClient.userApi.changePsw(captchaCode.value ?: "", map) },
            resp = {
                ToastUtils.show(mContext.getString(R.string.modify_success))
                isChangePswSuccess.postValue(true)
            },
            isShowDialog = true
        )

    }

    fun copyDevAccount() {
        val appType = MMKV.defaultMMKV().getInt(KV.APP_TYPE, 0)
        if (appType == AppType.DOU_YIN) {
            copyToClipboard("103447750")
        } else {
            copyToClipboard("followappdeveloper")
        }
        ToastUtils.show(mContext.getString(R.string.copy_dev_account))
    }


}