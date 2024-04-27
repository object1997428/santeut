package com.ssafy.santeut.data.repository

import android.util.Log
import com.ssafy.santeut.data.apiservice.AuthApiService
import com.ssafy.santeut.data.model.request.LoginRequest
import com.ssafy.santeut.data.model.request.SignUpRequest
import com.ssafy.santeut.data.model.response.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

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
        if(response.code() == 200){
            response.body()?.let {
                emit(response.body()!!)
            }
        }
    }

    override suspend fun signup(signUpRequest: SignUpRequest): Flow<Unit>  = flow {
        val response = authApiService.signup(signUpRequest)
        if(response.code() == 200){
            response.body()?.let {
                emit(response.body()!!)
            }
        }
    }
}