package com.santeut.domain.usecase

import android.util.Log
import com.santeut.data.model.request.FCMTokenRequest
import com.santeut.data.repository.AuthRepository
import javax.inject.Inject

class FCMTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend fun execute(fcmTokenRequest: FCMTokenRequest) = authRepository.sendFCMToken(fcmTokenRequest)
}