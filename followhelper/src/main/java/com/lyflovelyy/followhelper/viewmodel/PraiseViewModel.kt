package com.lyflovelyy.followhelper.viewmodel

import android.content.Context
import android.util.Log
import com.lyflovelyy.followhelper.App
import com.lyflovelyy.followhelper.utils.formatLargeNumber
import com.lyflovelyy.followhelper.utils.getPraiseType
import com.lyflovelyy.followhelper.utils.isLogined
import com.mind.data.data.model.praise.PraiseAccountModel
import com.mind.data.data.model.praise.PraiseVideoModel
import com.mind.data.http.ApiClient
import com.mind.lib.base.BaseViewModel
import com.mind.lib.base.ViewModelEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @Author      : liuyufei
 * @Date        : on 2024-01-02 21:36.
 * @Description :
 */
@HiltViewModel
class PraiseViewModel @Inject constructor() : BaseViewModel() {

    val praiseCount by lazy { ViewModelEvent<String>() }
    val praiseAccount by lazy { ViewModelEvent<PraiseAccountModel>() }
    val praiseVideoList by lazy { ViewModelEvent<MutableList<PraiseVideoModel>>() }
    val enablePraiseVideoList by lazy { ViewModelEvent<MutableList<PraiseVideoModel>>() }
    val praiseVideo by lazy { ViewModelEvent<PraiseVideoModel>() }
    private val mContext: Context by lazy { App.getContext() }


    private var mPermiss: Pair<Boolean, Boolean>? = null


    /**
     * 获取点赞总数
     */
    fun getTotalPraiseCount() {
        loadHttp(request = { ApiClient.praiseApi.getTotalPraiseCount() }, resp = {
            it?.let {
                Log.e(javaClass.name, "getTotalPraiseCount:${it} ")
                praiseCount.postValue(formatLargeNumber(it))
            }

        }, err = {
            praiseCount.postValue(formatLargeNumber(8743609))
        }, isShowDialog = false
        )

    }

    /**
     * 获取个人绑定账户
     */
    fun getPraiseAccount() {
        if (isLogined()) {
            loadHttp(
                request = { ApiClient.praiseApi.getPraiseAccount(getPraiseType()) },
                resp = {
                    Log.e(javaClass.name, "getPraiseAccount:${it.toString()} ")
                    it?.let { praiseAccount.postValue(it) }
                },
                isShowDialog = false
            )
        }
    }

    /**
     * 自己的视频列表
     */
    fun getPraiseVideoList() {
        if (isLogined()) {
            loadHttp(
                request = { ApiClient.praiseApi.getPraiseVideoList(getPraiseType()) },
                resp = {
                    Log.e(javaClass.name, "getPraiseVideoList:${it.toString()} ")
                    it?.let { praiseVideoList.postValue(it) }
                },
                isShowDialog = false
            )
        }
    }


    override fun onCleared() {
        super.onCleared()
    }

    /**
     * 添加或者更新点赞视频
     */
    fun addPraiseVideo(praiseVideoModel: PraiseVideoModel?, title: String, findUrl: String) {
        val postPraiseVideoModel =
            PraiseVideoModel(title = title, followType = getPraiseType(), url = findUrl)
        praiseVideoModel?.let {
            postPraiseVideoModel.id = it.id
        }
        loadHttp(request = { ApiClient.praiseApi.addOrUpdatePraiseVideo(postPraiseVideoModel) },
            resp = {
                it?.let {
                    praiseVideo.postValue(it)
                }
            })

    }

    fun deletePraise(item: PraiseVideoModel, back: (Boolean) -> Unit = { }) {
        loadHttp(request = { ApiClient.praiseApi.deletePraiseVideo(item.id) }, resp = {
            it?.let {
                back(it)
            }
        })
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