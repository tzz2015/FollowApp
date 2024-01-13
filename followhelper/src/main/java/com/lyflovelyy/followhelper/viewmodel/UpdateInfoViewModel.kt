package com.lyflovelyy.followhelper.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.linsh.utilseverywhere.StringUtils
import com.linsh.utilseverywhere.ToastUtils
import com.lyflovelyy.followhelper.App
import com.lyflovelyy.followhelper.R
import com.lyflovelyy.followhelper.utils.getFollowType
import com.mind.data.data.mmkv.KV
import com.mind.data.data.model.FollowAccount
import com.mind.data.data.model.SuggestionModel
import com.mind.data.data.model.UserModel
import com.mind.data.http.ApiClient
import com.mind.lib.util.CacheManager
import com.tencent.mmkv.MMKV

/**
 * @Author      : liuyufei
 * @Date        : on 2023-08-06 09:50.
 * @Description :
 */
class UpdateInfoViewModel : UserViewModel() {

    val text = MutableLiveData<String>()
    private var title: String = ""
    private val mContext: Context by lazy { App.getContext() }

    /**
     * 修改成功
     */
    val isChangeSuccess = MutableLiveData<Boolean>().apply { this.value = false }

    /**
     * 修改密码 邮箱 抖音号
     */
    fun change() {
        if (text.value.isNullOrEmpty()) {
            ToastUtils.show(mContext.getString(R.string.input_not_empty))
            return
        }
        when (title) {
            mContext.getString(R.string.add_bind_account), mContext.getString(R.string.modify_bind_account) -> {
                val account =
                    FollowAccount(account = text.value, followType = getFollowType())
                updateFollowAccount(account)
            }
            mContext.getString(R.string.modify_username) -> {
                val user = UserModel(phone = text.value)
                updateUser(user)
            }
            mContext.getString(R.string.modify_email) -> {
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
                    ToastUtils.show(mContext.getString(R.string.modify_success))
                    CacheManager.instance.putDYAccount(it.account)
                    isChangeSuccess.postValue(true)
                }

            },
            isShowDialog = true
        )
    }

    private fun updateUser(user: UserModel) {
        loadHttp(
            request = { ApiClient.userApi.updateUserInfo(user) },
            resp = {
                it?.run {
                    ToastUtils.show(mContext.getString(R.string.modify_success))
                    CacheManager.instance.putPhone(it.phone)
                    CacheManager.instance.putEmail(it.email)
                    updateLocalUserInfo()
                    isChangeSuccess.postValue(true)
                }

            },
            isShowDialog = true
        )
    }

    fun changeText(title: String) {
        this.title = title
        when (title) {
            mContext.getString(R.string.modify_bind_account) -> text.postValue(CacheManager.instance.getDYAccount())
            mContext.getString(R.string.modify_username) -> text.postValue(CacheManager.instance.getPhone())
            mContext.getString(R.string.modify_email) -> text.postValue(CacheManager.instance.getEmail())
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

    /**
     * 提交意见反馈
     */
    fun suggestion() {
        if (text.value.isNullOrEmpty()) {
            ToastUtils.show(mContext.getString(R.string.input_not_empty))
            return
        }
        val suggestionModel = SuggestionModel(text.value ?: "")
        loadHttp(
            request = { ApiClient.otherApi.postSuggestion(suggestionModel) },
            resp = {
                it?.run {
                    ToastUtils.show(mContext.getString(R.string.thinks_feedback))
                    isChangeSuccess.postValue(true)
                }

            }
        )
    }
}