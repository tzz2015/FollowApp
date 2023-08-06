package com.stardust.auojs.inrt.ui.mine

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.linsh.utilseverywhere.StringUtils
import com.linsh.utilseverywhere.ToastUtils
import com.mind.data.data.mmkv.KV
import com.mind.data.data.model.FollowAccount
import com.mind.data.data.model.FollowAccountType
import com.mind.data.data.model.FunctionType
import com.mind.data.data.model.UserModel
import com.mind.data.http.ApiClient
import com.mind.lib.util.CacheManager
import com.stardust.auojs.inrt.ui.home.UserViewModel
import com.tencent.mmkv.MMKV

/**
 * @Author      : liuyufei
 * @Date        : on 2023-08-06 09:50.
 * @Description :
 */
class UpdateInfoViewModel : UserViewModel() {

    val text = MutableLiveData<String>()
    private var title: String = ""
    /**
     * 修改成功
     */
    val isChangeSuccess = MutableLiveData<Boolean>().apply { this.value = false }
    /**
     * 修改密码 邮箱 抖音号
     */
    fun change() {
        when (title) {
            FunctionType.ADD_DOEYIN_ACCOUNT, FunctionType.CHANGE_DOEYIN_ACCOUNT -> {
                val account =
                    FollowAccount(account = text.value, followType = FollowAccountType.DOU_YIN)
                updateFollowAccount(account)
            }
            FunctionType.CHANGE_PHONE -> {
                val user = UserModel(phone = text.value)
                updateUser(user)
            }
            FunctionType.CHANGE_EMAIL -> {
                val user = UserModel(email = text.value)
                updateUser(user)
            }
        }
    }

    private fun updateFollowAccount(account: FollowAccount) {
        loadHttp(
            request = { ApiClient.followAccountApi.updateAccount(account) },
            resp = {
                it?.run {
                    ToastUtils.show("修改成功")
                    CacheManager.instance.putDYAccount(it.account)
                    isChangeSuccess.postValue(true)
                }

            }
        )
    }

    private fun updateUser(user: UserModel) {
        loadHttp(
            request = { ApiClient.userApi.updateUserInfo(user) },
            resp = {
                it?.run {
                    ToastUtils.show("修改成功")
                    CacheManager.instance.putPhone(it.phone)
                    CacheManager.instance.putEmail(it.email)
                    updateLocalUserInfo()
                    isChangeSuccess.postValue(true)
                }

            }
        )
    }

    fun changeText(title: String) {
        this.title = title
        when (title) {
            FunctionType.CHANGE_DOEYIN_ACCOUNT -> text.postValue(CacheManager.instance.getDYAccount())
            FunctionType.CHANGE_PHONE -> text.postValue(CacheManager.instance.getPhone())
            FunctionType.CHANGE_EMAIL -> text.postValue(CacheManager.instance.getEmail())
        }
    }

    private fun updateLocalUserInfo() {
        val userInfo = MMKV.defaultMMKV().getString(KV.USER_INFO, "") ?: ""
        if (StringUtils.isNotAllEmpty(userInfo)) {
            val gson = Gson()
            val userModel = gson.fromJson(userInfo, UserModel::class.java)
            userModel.phone = CacheManager.instance.getPhone()
            userModel.email = CacheManager.instance.getEmail()
            MMKV.defaultMMKV().putString(KV.USER_INFO, gson.toJson(userModel))
        }
    }
}