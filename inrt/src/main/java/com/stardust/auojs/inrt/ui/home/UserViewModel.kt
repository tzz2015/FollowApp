package com.stardust.auojs.inrt.ui.home

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.linsh.utilseverywhere.LogUtils
import com.linsh.utilseverywhere.StringUtils
import com.linsh.utilseverywhere.ToastUtils
import com.mind.data.data.model.FollowAccount
import com.mind.data.data.model.FollowAccountType
import com.mind.data.data.model.FunctionType
import com.mind.data.data.model.UserModel
import com.mind.data.http.ApiClient
import com.mind.lib.base.BaseViewModel
import com.mind.lib.base.ViewModelEvent
import com.mind.lib.util.CacheManager
import com.stardust.app.GlobalAppContext
import com.stardust.auojs.inrt.data.Constants
import com.stardust.auojs.inrt.ui.mine.*
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
                }
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
            ToastUtils.show("手机号不能为空")
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
            }
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
            toUpdateActivity(FunctionType.CHANGE_DOEYIN_ACCOUNT)
        } else {
            toUpdateActivity(FunctionType.ADD_DOEYIN_ACCOUNT)
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


    fun register() {
        if (phone.value.isNullOrEmpty()) {
            ToastUtils.show("手机号不能为空")
            return
        }
        if (password.value.isNullOrEmpty()) {
            ToastUtils.show("密码不能为空")
            return
        }
        if (email.value.isNullOrEmpty()) {
            ToastUtils.show("邮箱不能为空")
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
                ToastUtils.show("注册成功")
                isRegisterSuccess.postValue(true)
            }
        )
    }

    /**
     * 发送验证码
     */
    fun sendCode(view: View) {
        if (phone.value.isNullOrEmpty()) {
            ToastUtils.show("手机号不能为空")
            return
        }
        if (email.value.isNullOrEmpty()) {
            ToastUtils.show("邮箱不能为空")
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
                ToastUtils.show("发送验证码成功")
                view.isEnabled = true
            },
            err = {
                view.isEnabled = true
            }
        )

    }

    /**
     * 修改密码
     */
    fun changePsw() {
        if (phone.value.isNullOrEmpty()) {
            ToastUtils.show("手机号不能为空")
            return
        }
        if (password.value.isNullOrEmpty()) {
            ToastUtils.show("密码不能为空")
            return
        }
        if (email.value.isNullOrEmpty()) {
            ToastUtils.show("邮箱不能为空")
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
                ToastUtils.show("修改成功")
                isChangePswSuccess.postValue(true)
            }
        )

    }

    fun copyDevAccount() {
        copyToClipboard("103447750")
    }

    fun copyToClipboard(text: String) {
        try {
            // 获取剪切板管理器实例
            val clipboardManager: ClipboardManager =
                GlobalAppContext.get()
                    .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            // 要复制的文本
            // 创建一个 ClipData 对象，包含要复制的文本
            val clipData: ClipData = ClipData.newPlainText("label", text)
            // 将 ClipData 对象放入剪切板
            clipboardManager.setPrimaryClip(clipData)
            ToastUtils.show("已复制开发者抖音号到剪切板")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}