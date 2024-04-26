package com.ssafy.santeut.data.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun getToken(): Flow<String>
}