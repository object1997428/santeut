package com.santeut.domain.usecase

import com.santeut.data.model.request.CreateGuildPostRequest
import com.santeut.data.model.response.GuildPostDetailResponse
import com.santeut.data.model.response.GuildPostResponse
import com.santeut.data.model.response.GuildResponse
import com.santeut.data.repository.GuildRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class GuildUseCase @Inject constructor(
    private val guildRepository: GuildRepository
) {

    suspend fun getGuilds(): List<GuildResponse> =
        guildRepository.getGuilds()


    suspend fun myGuilds(): List<GuildResponse> =
        guildRepository.myGuilds()


    suspend fun getGuild(guildId: Int): GuildResponse =
        guildRepository.getGuild(guildId)


    suspend fun applyGuild(guildId: Int): Flow<Unit> =
        guildRepository.applyGuild(guildId)


    suspend fun getGuildPostList(guildId: Int, categoryId: Int): List<GuildPostResponse> =
        guildRepository.getGuildPostList(guildId, categoryId)


    suspend fun createGuildPost(
        images: List<MultipartBody.Part>,
        createGuildPostRequest: CreateGuildPostRequest
    ): Flow<Unit> = guildRepository.createGuildPost(images, createGuildPostRequest)

    suspend fun getGuildPost(guildPostId: Int): GuildPostDetailResponse =
        guildRepository.getGuildPost(guildPostId)

}