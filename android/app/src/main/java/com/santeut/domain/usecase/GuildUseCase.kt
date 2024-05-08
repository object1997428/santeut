package com.santeut.domain.usecase

import com.santeut.data.model.response.GuildListResponse
import com.santeut.data.model.response.GuildResponse
import com.santeut.data.repository.GuildRepository
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
}