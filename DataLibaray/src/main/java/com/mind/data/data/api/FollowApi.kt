package com.mind.data.data.api

import com.mind.lib.data.model.Res
import retrofit2.http.GET

/**
 * @Author      : liuyufei
 * @Date        : on 2023-07-23 14:55.
 * @Description :描述
 */
interface FollowApi {

    /**
     * 累计关注列表
     */
    @GET("follow/count")
    suspend fun getTotalFollowCount(): Res<Long>
}