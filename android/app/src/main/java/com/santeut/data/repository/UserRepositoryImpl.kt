package com.santeut.data.repository

import android.util.Log
import com.santeut.data.apiservice.UserApiService
import com.santeut.data.model.response.MyProfileResponse
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService
) : UserRepository {
    override suspend fun getMyProfile(): MyProfileResponse {
        return try {
            val response = userApiService.getMyProfile()
            if (response.status == "200") {
                response.data
            } else {
                throw Exception("Failed to load post: ${response.status} ${response.data}")
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Network error while fetching post: ${e.message}", e)
            throw e
        }
    }
}