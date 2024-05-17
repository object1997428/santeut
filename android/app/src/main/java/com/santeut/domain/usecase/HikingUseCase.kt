package com.santeut.domain.usecase

import com.santeut.data.model.request.StartHikingRequest
import com.santeut.data.model.response.StartHikingResponse
import com.santeut.data.repository.HikingRepository
import javax.inject.Inject

class HikingUseCase @Inject constructor(
    private val hikingRepository: HikingRepository
) {
    suspend fun startHiking(
        startHikingRequest: StartHikingRequest
    ): StartHikingResponse = hikingRepository.startHiking(startHikingRequest)
}