package com.mind.data.data.api


import com.mind.data.data.model.AnnouncementModel
import com.mind.data.data.model.ScriptModel
import com.mind.data.data.model.SpreadModel
import com.mind.data.data.model.SuggestionModel
import com.mind.lib.data.model.Res
import retrofit2.http.*

interface ApiService {


    @GET("spread/list")
    suspend fun getSpreadList(): Res<MutableList<SpreadModel>>

    @GET("announcement/list")
    suspend fun getAnnouncementList(): Res<MutableList<AnnouncementModel>>

    @POST("suggestion/add")
    suspend fun postSuggestion(@Body suggestionModel: SuggestionModel): Res<SuggestionModel>

    @POST("script/find")
    suspend fun findScript(@Field("version") version:Int,@Field("isDebug") isDebug:Boolean): Res<ScriptModel?>



}