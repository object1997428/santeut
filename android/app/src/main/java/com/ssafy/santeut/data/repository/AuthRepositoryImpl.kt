package com.ssafy.santeut.data.repository

import com.ssafy.santeut.data.apiservice.AuthApiService
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
}