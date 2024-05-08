package com.santeut.data.apiservice

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.response.GuildListResponse
import retrofit2.http.GET

interface GuildApiService {

    @GET("/api/guild/list")
    suspend fun getGuilds(): CustomResponse<GuildListResponse>

}