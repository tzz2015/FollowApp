package com.lyflovelyy.followhelper.viewmodel

import android.util.Log
import com.lyflovelyy.followhelper.utils.getFollowType
import com.lyflovelyy.followhelper.utils.getPraiseType
import com.lyflovelyy.followhelper.utils.isLogined
import com.mind.data.data.model.FollowAccount
import com.mind.data.data.model.praise.PraiseVideoModel
import com.mind.data.http.ApiClient
import com.mind.lib.base.BaseViewModel
import com.mind.lib.base.ViewModelEvent
import javax.inject.Inject

/**
 * @Author      : liuyufei
 * @Date        : on 2024-01-13 16:43.
 * @Description :
 */
class GuideViewModel @Inject constructor() : BaseViewModel() {
    val enablePraiseVideoList by lazy { ViewModelEvent<MutableList<PraiseVideoModel>>() }

    fun getCopyList(type: Int) {
        val list: MutableList<PraiseVideoModel> = ArrayList()
        if (type == 0) {
            getEnableFollowList {
                for (item in it) {
                    list.add(PraiseVideoModel(title = item.account ?: ""))
                }
                enablePraiseVideoList.postValue(list)
            }
        } else {
               getEnablePraiseList {
                   for (item in it) {
                       list.add(PraiseVideoModel(title = item.url))
                   }
                   enablePraiseVideoList.postValue(list)
               }
        }
    }

    private fun getEnableFollowList(back: (MutableList<FollowAccount>) -> Unit) {
        if (isLogined()) {
            loadHttp(
                request = { ApiClient.followAccountApi.getEnableFollowList(getFollowType()) },
                resp = {
                    it?.let {
                        back(it)
                    }
                },
                isShowDialog = true
            )
        }
    }

    /**
     * 获取可点赞列表
     */
    private fun getEnablePraiseList(back: (MutableList<PraiseVideoModel>) -> Unit) {
        if (isLogined()) {
            loadHttp(
                request = { ApiClient.praiseApi.getEnablePraiseList(getPraiseType()) },
                resp = {
                    Log.e(javaClass.name, "getEnablePraiseList:${it.toString()} ")
                    it?.let {
                        back(it)
                    }
                },
                isShowDialog = true
            )
        }
    }
}