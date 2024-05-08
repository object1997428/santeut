package com.santeut.data.repository

import android.util.Log
import com.santeut.data.apiservice.GuildApiService
import com.santeut.data.model.response.GuildListResponse
import com.santeut.data.model.response.GuildResponse
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
                response.data.guildList
            } else {
                throw Exception("Failed to load post: ${response.status} ${response.data}")
            }
        } catch (e: Exception) {
            Log.e("GuildRepository", "Network error: ${e.message}", e)
            emptyList()
        }
    }
}