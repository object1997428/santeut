package com.santeut.domain.usecase

import com.santeut.data.model.response.GuildListResponse
import com.santeut.data.model.response.GuildResponse
import com.santeut.data.repository.GuildRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GuildUseCase @Inject constructor(
    private val guildRepository: GuildRepository
) {

    suspend fun getGuilds(): List<GuildResponse> {
        return guildRepository.getGuilds()
    }

    suspend fun myGuilds(): List<GuildResponse> {
        return guildRepository.myGuilds()
    }

    suspend fun getGuild(guildId: Int): GuildResponse {
        return guildRepository.getGuild(guildId)
    }

    suspend fun applyGuild(guildId: Int): Flow<Unit> {
        return guildRepository.applyGuild(guildId)
    }
}