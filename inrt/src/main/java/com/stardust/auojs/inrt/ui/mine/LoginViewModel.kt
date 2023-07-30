package com.stardust.auojs.inrt.ui.mine

import androidx.lifecycle.MutableLiveData
import com.linsh.utilseverywhere.LogUtils
import com.linsh.utilseverywhere.ToastUtils
import com.mind.data.data.model.UserModel
import com.mind.data.http.ApiClient.userApi
import com.mind.lib.base.BaseViewModel
import com.stardust.auojs.inrt.data.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

/**
 * @Author      : liuyufei
 * @Date        : on 2023-07-30 15:55.
 * @Description :
 */
@HiltViewModel
class LoginViewModel @Inject constructor() : BaseViewModel() {
    //是否显示密码  默认不显示
    val isClose = MutableLiveData<Boolean>().apply { this.value = true }

    //账号
    val phone = MutableLiveData<String>()

    //密码
    val password = MutableLiveData<String>()

    val loginResult by lazy { MutableLiveData<UserModel>() }


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
            request = { userApi.login(map) },
            resp = {
                ToastUtils.show("登录成功")
                LogUtils.e(it.toString())
                loginResult.postValue(it)
            },
            isShowDialog = true
        )
    }
}


