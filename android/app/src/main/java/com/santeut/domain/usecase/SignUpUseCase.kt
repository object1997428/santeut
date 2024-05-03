package com.santeut.domain.usecase

import com.santeut.data.model.request.SignUpRequest
import com.santeut.data.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend fun excute(signUpRequest: SignUpRequest) = authRepository.signup(signUpRequest)
}
