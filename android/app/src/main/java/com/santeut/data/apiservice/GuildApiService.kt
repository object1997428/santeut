package com.santeut.data.apiservice

import com.google.gson.Gson
import com.santeut.data.model.CustomResponse
import com.santeut.data.model.request.CreateGuildPostRequest
import com.santeut.data.model.response.GuildListResponse
import com.santeut.data.model.response.GuildPostListResponse
import com.santeut.data.model.response.GuildResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface GuildApiService {

    @GET("/api/guild/list")
    suspend fun getGuilds(): CustomResponse<GuildListResponse>

    @GET("/api/guild/myguild")
    suspend fun myGuilds(): CustomResponse<GuildListResponse>

    @GET("/api/guild/{guildId}")
    suspend fun getGuild(
        @Path("guildId") guildId: Int
    ): CustomResponse<GuildResponse>

    @POST("/api/guild/user/apply/{guildId}")
    suspend fun applyGuild(
        @Path("guildId") guildId: Int
    ): CustomResponse<Unit>

    @GET("/api/guild/post/{guildId}/{categoryId}")
    suspend fun getGuildPostList(
        @Path("guildId") guildId: Int,
        @Path("categoryId") categoryId: Int
    ): CustomResponse<GuildPostListResponse>

    @POST("/api/guild/post")
    @Multipart
    suspend fun createGuildPost(
        @Part images: List<MultipartBody.Part>,
        @Part createGuildPostRequest: MultipartBody.Part
    ): CustomResponse<Unit>

}