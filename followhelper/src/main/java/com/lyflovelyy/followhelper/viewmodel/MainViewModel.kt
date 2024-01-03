package com.lyflovelyy.followhelper.viewmodel

import com.lyflovelyy.followhelper.utils.isLogined
import com.mind.data.data.mmkv.KV
import com.mind.data.http.ApiClient
import com.mind.lib.base.BaseViewModel
import com.tencent.mmkv.MMKV

/**
 * @Author      : liuyufei
 * @Date        : on 2023-08-17 22:49.
 * @Description :
 */
class MainViewModel : BaseViewModel() {

    fun getAdSwitch() {
        if (!isLogined()) {
            return
        }
        loadHttp(
            request = {
                ApiClient.otherApi.getAdSwitch(
                    com.linsh.utilseverywhere.AppUtils.getVersionName()
                )
            },
            resp = {
                it?.let {
                    com.linsh.utilseverywhere.LogUtils.e("getAdSwitch:$it")
                    MMKV.defaultMMKV().putBoolean(KV.START_SWITCH, it.startSwitch)
                    MMKV.defaultMMKV().putBoolean(KV.BANNER_SWITCH, it.bannerSwitch)
                    MMKV.defaultMMKV().putInt(KV.FOLLOW_SWITCH_NUM, it.followSwitchNum)
                    MMKV.defaultMMKV().putBoolean(KV.TABLE_SWITCH, it.tableSwitch)
                    MMKV.defaultMMKV().putBoolean(KV.RESERVE_SWITCH, it.reserveSwitch)
                }
            },
            isShowDialog = false
        )
    }
}