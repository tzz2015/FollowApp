package com.mind.data.data.api




import com.mind.data.data.model.AnnouncementModel
import com.mind.data.data.model.SpreadModel
import com.mind.lib.data.model.Res
import retrofit2.http.GET

interface ApiService {



    @GET("spread/list")
    suspend fun getSpreadList(): Res<MutableList<SpreadModel>>

    @GET("announcement/list")
    suspend fun getAnnouncementList(): Res<MutableList<AnnouncementModel>>




}