package com.santeut.data.repository

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.response.MountainListResponse
import retrofit2.Call

interface MountainRepository {

    suspend fun searchMountain(name: String, region: String?): MountainListResponse
}
