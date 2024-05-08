package com.santeut.data.repository

import javax.inject.Inject

class GuildRepositoryImpl @Inject constructor(
    private val guildRepository: GuildRepository
) : GuildRepository {
}