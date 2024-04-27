package com.ssafy.santeut.data.repository

import com.ssafy.santeut.data.model.request.LoginRequest
import com.ssafy.santeut.data.model.request.SignUpRequest
import com.ssafy.santeut.data.model.response.LoginResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun getToken(): Flow<String>
    suspend fun login(loginRequest: LoginRequest): Flow<LoginResponse>
    suspend fun signup(signUpRequest: SignUpRequest): Flow<Unit>
}