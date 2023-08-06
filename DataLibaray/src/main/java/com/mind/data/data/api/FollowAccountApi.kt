package com.mind.data.data.api

import com.mind.data.data.model.FollowAccount
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
interface FollowAccountApi {

    /**
     * 关注的账户
     */
    @GET("followAccount/account/{followType}")
    suspend fun getFollowAccount(@Path("followType") followType: Int): Res<FollowAccount>

    /**
     * 添加或者修改账户
     */
    @POST("followAccount/updateAccount")
    suspend fun updateAccount(@Body followAccount: FollowAccount): Res<FollowAccount>
}