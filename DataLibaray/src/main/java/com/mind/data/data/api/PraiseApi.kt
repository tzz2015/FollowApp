package com.mind.data.data.api

import com.mind.data.data.model.praise.PraiseAccountModel
import com.mind.data.data.model.praise.PraiseVideoModel
import com.mind.lib.data.model.Res
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * @Author      : liuyufei
 * @Date        : on 2023-07-23 14:55.
 * @Description :描述
 */
interface PraiseApi {

    /**
     * 点赞总数
     */
    @GET("praise/count")
    suspend fun getTotalPraiseCount(): Res<Long>

    /**
     * 获取点赞的账户信息
     */
    @GET("praiseAccount/account/{followType}")
    suspend fun getPraiseAccount(@Path("followType") followType: Int): Res<PraiseAccountModel?>

    /**
     * 获取点赞的账户信息
     */
    @GET("praiseVideo/praiseList/{followType}")
    suspend fun getPraiseVideoList(@Path("followType") followType: Int): Res<MutableList<PraiseVideoModel>?>

    /**
     * 获取点赞的账户信息
     */
    @POST("praiseVideo/update")
    suspend fun addOrUpdatePraiseVideo(@Body praiseVideo: PraiseVideoModel): Res<PraiseVideoModel?>


}