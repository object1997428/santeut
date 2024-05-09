package com.santeut.data.repository

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.response.MountainListResponse

interface MountainRepository {

    suspend fun searchMountain(name: String, region: String?): CustomResponse<MountainListResponse>
}