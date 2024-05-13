package com.santeut.data.repository

import android.util.Log
import com.santeut.data.apiservice.AuthApiService
import com.santeut.data.model.request.FCMTokenRequest
import com.santeut.data.model.request.LoginRequest
import com.santeut.data.model.request.SignUpRequest
import com.santeut.data.model.response.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService
): AuthRepository {
    override suspend fun getToken(): Flow<String> = flow {
        val response = authApiService.getToken()
        if(response.code() == 200){
            response.body()?.let {
                emit(response.body()!!)
            }
        }
    }
    override suspend fun login(loginRequest: LoginRequest): Flow<LoginResponse> = flow {
        val response = authApiService.login(loginRequest)
        if(response.status == "200"){
            emit(response.data)
        }
    }

    override suspend fun signup(signUpRequest: SignUpRequest): Flow<Unit>  = flow {
        val response = authApiService.signup(signUpRequest)
        if(response.status == "200"){
            emit(response.data)
        }
    }

    override suspend fun sendFCMToken(fcmTokenRequest: FCMTokenRequest): Flow<Unit> = flow {
        val response = authApiService.sendFCMToken(fcmTokenRequest)
        if(response.status == "200"){
            emit(response.data)
        }
    }
}