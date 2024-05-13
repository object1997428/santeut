package com.santeut.data.repository

import com.santeut.data.model.response.GuildListResponse
import com.santeut.data.model.response.GuildResponse
import kotlinx.coroutines.flow.Flow

interface GuildRepository {

    suspend fun getGuilds(): List<GuildResponse>

    suspend fun myGuilds(): List<GuildResponse>

    suspend fun getGuild(guildId: Int): GuildResponse

    suspend fun applyGuild(guildId: Int): Flow<Unit>
}