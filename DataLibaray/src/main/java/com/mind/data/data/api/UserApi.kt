package com.mind.data.data.api


import com.mind.data.data.model.UserModel
import com.mind.lib.data.model.Res
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * 用户相关
 */
interface UserApi {
    /**
     * 登录
     */
    @POST("login")
    suspend fun login(@Body map: HashMap<String, String>): Res<UserModel?>


    /**
     * 总用户数
     */
    @GET("userList")
    suspend fun getTotalUserCount(): Res<Long>


    @POST("logout")
    suspend fun logout(): Res<String?>

    /**
     * 注册
     */
    @POST("create")
    suspend fun register(@Body map: HashMap<String, String>): Res<UserModel?>

    /**
     * 发送验证码
     */
    @POST("sendCode")
    suspend fun senCode(@Body map: HashMap<String, String>): Res<String?>

    /**
     * 发送验证码
     */
    @POST("changePsw/{code}")
    suspend fun changePsw(
        @Path("code") code: String,
        @Body map: HashMap<String, String>
    ): Res<UserModel?>

    /**
     * 更新手机号码 邮箱
     */
    @POST("updateUser")
    suspend fun updateUserInfo(
        @Body map: HashMap<String, String>
    ): Res<UserModel?>


}