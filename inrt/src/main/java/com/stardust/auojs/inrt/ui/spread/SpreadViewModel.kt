package com.stardust.auojs.inrt.ui.spread

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mind.data.data.mmkv.KV
import com.mind.data.data.model.SpreadModel
import com.mind.data.http.ApiClient
import com.mind.lib.base.BaseViewModel
import com.mind.lib.base.ViewModelEvent
import com.stardust.auojs.inrt.data.Constants
import com.stardust.auojs.inrt.util.isZh
import com.tencent.mmkv.MMKV
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SpreadViewModel @Inject constructor() : BaseViewModel() {
    val spreadList by lazy { ViewModelEvent<MutableList<SpreadModel>>() }


    fun getSpreadList() {
        if (Constants.SPREAD_URL != null) {
            spreadList.postValue(Constants.SPREAD_URL)
            return
        }
        // 不显示开屏广告即为要显示隐私协议
        val showPrivacy = !MMKV.defaultMMKV().getBoolean(KV.START_SWITCH, false)
        if (showPrivacy) {
            val url = if (isZh()) "file:////android_asset/privacy_cn.html"
            else "file:////android_asset/privacy.html"
            saveLocal(arrayListOf(SpreadModel("", url)),false)
            return
        }
        loadHttp(
            request = { ApiClient.otherApi.getSpreadList() },
            resp = {
                Log.e(javaClass.name, "getSpreadList:${it.toString()} ")
                it?.let {
                    saveLocal(it)
                }

            },
            err = {
                loadLocal()
            },
            isShowDialog = false
        )
    }

    private fun loadLocal() {
        val spread = MMKV.defaultMMKV().getString(KV.SPREAD_URL, null)
        if (spread != null) {
            val gson = Gson()
            val list: MutableList<SpreadModel> =
                gson.fromJson(spread, object : TypeToken<List<SpreadModel>?>() {}.type)
            saveLocal(list, false)
        } else {
            val list: MutableList<SpreadModel> = ArrayList()
            saveLocal(list)
        }
    }

    private fun saveLocal(it: MutableList<SpreadModel>, isAdd: Boolean = true) {
        if (isAdd) {
            it.add(SpreadModel(Constants.AD_VIDEO, Constants.AD_VIDEO))
            it.add(SpreadModel("视频教程", Constants.TUTORIAL_URL))
        }
        Constants.SPREAD_URL = it
        val gson = Gson()
        MMKV.defaultMMKV().putString(KV.SPREAD_URL, gson.toJson(it))
        spreadList.postValue(it)
    }


}