package com.santeut.data.repository

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.response.MountainListResponse
import com.santeut.data.model.response.MountainResponse
import retrofit2.Call

interface MountainRepository {

    suspend fun popularMountain(): List<MountainResponse>
    suspend fun searchMountain(name: String, region: String?): List<MountainResponse>
}
