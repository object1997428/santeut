package com.santeut.data.repository

import com.santeut.data.model.response.GuildListResponse
import com.santeut.data.model.response.GuildResponse

interface GuildRepository {

    suspend fun getGuilds(): List<GuildResponse>
}