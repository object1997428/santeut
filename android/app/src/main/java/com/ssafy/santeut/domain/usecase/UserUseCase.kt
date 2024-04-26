package com.ssafy.santeut.domain.usecase

import com.ssafy.santeut.data.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend fun getToken() = authRepository.getToken()

    // 단일 usecase인 경우는
    //    fun excute()
}