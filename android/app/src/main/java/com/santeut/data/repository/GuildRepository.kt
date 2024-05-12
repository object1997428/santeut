package com.santeut.data.repository

import com.santeut.data.model.request.CreateGuildPostRequest
import com.santeut.data.model.response.GuildMemberResponse
import com.santeut.data.model.response.GuildPostDetailResponse
import com.santeut.data.model.response.GuildPostResponse
import com.santeut.data.model.response.GuildResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface GuildRepository {

    suspend fun getGuilds(): List<GuildResponse>

    suspend fun myGuilds(): List<GuildResponse>

    suspend fun getGuild(guildId: Int): GuildResponse

    suspend fun applyGuild(guildId: Int): Flow<Unit>

    suspend fun getGuildPostList(guildId: Int, categoryId: Int): List<GuildPostResponse>

    suspend fun createGuildPost(
        images: List<MultipartBody.Part>,
        createGuildPostRequest: CreateGuildPostRequest
    ): Flow<Unit>

    suspend fun getGuildPost(guildPostId: Int): GuildPostDetailResponse

    suspend fun getGuildMemberList(guildId: Int): List<GuildMemberResponse>

    suspend fun exileMember(guildId: Int, userId: Int): Flow<Unit>

    suspend fun changeLeader(guildId: Int, newLeaderId: Int): Flow<Unit>

    suspend fun quitGuild(guildId: Int): Flow<Unit>

}