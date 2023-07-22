package com.mind.data.data.api


import com.mind.data.data.model.LoginModel
import com.mind.lib.data.model.Res
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * 用户相关
 */
interface UserApi {
    /**
     * 登录
     */
    @POST("/login")
    suspend fun login(@Body map: HashMap<String, String>): Res<LoginModel?>


    /**
     * 收藏
     */
    @GET("userList")
    suspend fun getTotalUserCount(): Res<Long>



}