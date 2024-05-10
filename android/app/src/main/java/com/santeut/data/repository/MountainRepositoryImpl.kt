package com.santeut.data.repository

import android.util.Log
import com.santeut.data.apiservice.MountainApiService
import com.santeut.data.model.CustomResponse
import com.santeut.data.model.response.MountainListResponse
import com.santeut.data.model.response.MountainResponse
import retrofit2.Call
import java.lang.Exception
import javax.inject.Inject

class MountainRepositoryImpl @Inject constructor(
    private val mountainApiService: MountainApiService
) : MountainRepository {
    override suspend fun searchMountain(name: String, region: String?): List<MountainResponse> {
        return try{
            val response = mountainApiService.searchMountain(name, region)
            if(response.status=="200"){
                response.data.result?: emptyList()
            } else{
                Log.e(
                    "MountainRepository",
                    "산 목록 조회 실패: ${response.status} - ${response.data}"
                )
                emptyList()
            }
        } catch (e: Exception){
            Log.e("MountainRepository", "Network error while fetching post: ${e.message}", e)
            throw e
        }
    }
}