package com.santeut.domain.usecase

import com.santeut.data.repository.MountainRepository
import javax.inject.Inject

class MountainUseCase @Inject constructor(
    private val mountainRepository: MountainRepository
) {
}