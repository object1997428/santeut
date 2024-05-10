package com.santeut.domain.usecase

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.response.MountainListResponse
import com.santeut.data.model.response.MountainResponse
import com.santeut.data.repository.MountainRepository
import javax.inject.Inject

class MountainUseCase @Inject constructor(
    private val mountainRepository: MountainRepository
) {
    suspend fun searchMountain(name: String, region: String?): MountainListResponse =
        mountainRepository.searchMountain(name, region)
}