package com.santeut.data.repository

import android.util.Log
import com.santeut.data.apiservice.GuildApiService
import com.santeut.data.model.response.GuildListResponse
import com.santeut.data.model.response.GuildResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GuildRepositoryImpl @Inject constructor(
    private val guildApiService: GuildApiService
) : GuildRepository {
    override suspend fun getGuilds(): List<GuildResponse> {
        return try {
            val response = guildApiService.getGuilds()
            if (response.status == "200") {
                response.data.guildList
            } else {
                throw Exception("Failed to load post: ${response.status} ${response.data}")
            }
        } catch (e: Exception) {
            Log.e("GuildRepository", "Network error: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun myGuilds(): List<GuildResponse> {
        return try {
            val response = guildApiService.myGuilds()
            if (response.status == "200") {
                response.data.guildList ?: emptyList()
            } else {
                throw Exception("Failed to load post: ${response.status} ${response.data}")
            }
        } catch (e: Exception) {
            Log.e("GuildRepository", "Network error: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun getGuild(guildId: Int): GuildResponse {
        return try {
            val response = guildApiService.getGuild(guildId)
            if (response.status == "200") {
                response.data
            } else {
                throw Exception("Failed to load post: ${response.status} ${response.data}")
            }
        } catch (e: Exception) {
            Log.e("GuildRepository", "Network error while fetching post: ${e.message}", e)
            throw e
        }
    }

    override suspend fun applyGuild(guildId: Int): Flow<Unit> = flow {
        val response = guildApiService.applyGuild(guildId)
        if (response.status == "200") {
            Log.d("Guild Repository", "가입 요청 성공")
            emit(response.data)
        } else {
            throw Exception("가입 요청 실패: ${response.status} ${response.data}")
        }
    }
}