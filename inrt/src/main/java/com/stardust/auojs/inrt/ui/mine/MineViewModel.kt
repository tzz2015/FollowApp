package com.stardust.auojs.inrt.ui.mine

import android.util.Log
import com.mind.data.data.model.AnnouncementModel
import com.mind.data.http.ApiClient
import com.mind.lib.base.BaseViewModel
import com.mind.lib.base.ViewModelEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import org.autojs.autoxjs.inrt.R
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MineViewModel @Inject constructor() : BaseViewModel() {

    val announcementList by lazy { ViewModelEvent<MutableList<AnnouncementModel>>() }
    private val imageArray = arrayOf(
        R.mipmap.icon_header_1_1,
        R.mipmap.icon_header_1_2,
        R.mipmap.icon_header_1_3,
        R.mipmap.icon_header_1_4,
        R.mipmap.icon_header_1_5,
        R.mipmap.icon_header_1_6,
        R.mipmap.icon_header_1_7,
        R.mipmap.icon_header_1_8,
        R.mipmap.icon_header_1_9,
        R.mipmap.icon_header_1_10,
        R.mipmap.icon_header_1_11,
        R.mipmap.icon_header_1_12,
        R.mipmap.icon_header_1_13,
        R.mipmap.icon_header_1_14,
        R.mipmap.icon_header_1_15,
        R.mipmap.icon_header_1_16,
        R.mipmap.icon_header_1_17,
    )


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

    fun getHeadImage(): Int {
        val randomInt = Random.nextInt(0, imageArray.size)
        return imageArray[randomInt]
    }
}