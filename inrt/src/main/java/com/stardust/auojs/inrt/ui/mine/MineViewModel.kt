package com.stardust.auojs.inrt.ui.mine

import android.util.Log
import com.mind.data.data.model.AnnouncementModel
import com.mind.data.http.ApiClient
import com.mind.lib.base.BaseViewModel
import com.mind.lib.base.ViewModelEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MineViewModel @Inject constructor() : BaseViewModel() {

    val announcementList by lazy { ViewModelEvent<MutableList<AnnouncementModel>>() }


    fun getAnnouncementList() {
        loadHttp(
            request = { ApiClient.otherApi.getAnnouncementList() },
            resp = {
                Log.e(javaClass.name, "getAnnouncementList:${it.toString()} ")
                it?.let {
                    announcementList.postValue(it)
                }
            },
            isShowDialog = false
        )
    }
}