package com.santeut.domain.usecase

import com.santeut.data.model.request.LoginRequest
import com.santeut.data.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend fun excute(loginRequest: LoginRequest) = authRepository.login(loginRequest)
}
