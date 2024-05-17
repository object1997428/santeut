package com.santeut.data.repository

import android.util.Log
import com.santeut.data.apiservice.HikingApiService
import com.santeut.data.model.request.StartHikingRequest
import com.santeut.data.model.response.StartHikingResponse
import javax.inject.Inject

class HikingRepositoryImpl @Inject constructor(
    private val hikingApiService: HikingApiService
) : HikingRepository {
    override suspend fun startHiking(startHikingRequest: StartHikingRequest): StartHikingResponse {
        return try {
            val response = hikingApiService.startHiking(startHikingRequest)
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
}