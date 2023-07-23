package com.stardust.auojs.inrt.ui.spread

import android.util.Log
import com.mind.data.data.model.SpreadModel
import com.mind.data.http.ApiClient
import com.mind.lib.base.BaseViewModel
import com.mind.lib.base.ViewModelEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SpreadViewModel @Inject constructor() : BaseViewModel() {
    val spreadList by lazy { ViewModelEvent<MutableList<SpreadModel>>() }


    fun getSpreadList() {
        loadHttp(
            request = { ApiClient.otherApi.getSpreadList() },
            resp = {
                Log.e(javaClass.name, "getSpreadList:${it.toString()} ")
                it?.let {
                    spreadList.postValue(it)
                }
            },
            isShowDialog = false
        )
    }


}