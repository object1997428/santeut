package com.santeut.data.repository

import android.util.Log
import com.santeut.data.apiservice.MountainApiService
import com.santeut.data.model.CustomResponse
import com.santeut.data.model.response.MountainListResponse
import com.santeut.data.model.response.MountainResponse
import javax.inject.Inject

class MountainRepositoryImpl @Inject constructor(
    private val mountainApiService: MountainApiService
) : MountainRepository {
    override suspend fun searchMountain(name: String, region: String?): CustomResponse<MountainListResponse> {
        val response = mountainApiService.searchMountain(name, region).execute()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Response body is null")
        } else {
            throw Exception("산 목록 불러오기 실패: ${response.message()}")
        }
    }
}