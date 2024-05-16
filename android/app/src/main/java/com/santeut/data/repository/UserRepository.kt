package com.santeut.data.repository

import com.santeut.data.model.response.MyProfileResponse

interface UserRepository {
    suspend fun getMyProfile(): MyProfileResponse
}