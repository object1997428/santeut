package com.santeut.domain.usecase

import com.santeut.data.repository.HikingRepository
import javax.inject.Inject

class HikingUseCase @Inject constructor(
    private val hikingRepository: HikingRepository
) {
}