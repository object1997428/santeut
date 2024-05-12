package com.santeut.data.repository

import com.santeut.data.model.request.CreateGuildPostRequest
import com.santeut.data.model.response.GuildPostResponse
import com.santeut.data.model.response.GuildResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface GuildRepository {

    suspend fun getGuilds(): List<GuildResponse>

    suspend fun myGuilds(): List<GuildResponse>

    suspend fun getGuild(guildId: Int): GuildResponse

    suspend fun applyGuild(guildId: Int): Flow<Unit>

    suspend fun getGuildPostList(guildId:Int, categoryId:Int): List<GuildPostResponse>

    suspend fun createGuildPost(images:List<MultipartBody.Part>, createGuildPostRequest: CreateGuildPostRequest): Flow<Unit>
}