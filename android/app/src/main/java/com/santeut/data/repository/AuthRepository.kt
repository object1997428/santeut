package com.santeut.data.repository

import com.santeut.data.model.request.FCMTokenRequest
import com.santeut.data.model.request.LoginRequest
import com.santeut.data.model.request.SignUpRequest
import com.santeut.data.model.response.LoginResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun getToken(): Flow<String>
    suspend fun login(loginRequest: LoginRequest): Flow<LoginResponse>
    suspend fun signup(signUpRequest: SignUpRequest): Flow<Unit>
    suspend fun sendFCMToken(fcmTokenRequest: FCMTokenRequest): Flow<Unit>
}