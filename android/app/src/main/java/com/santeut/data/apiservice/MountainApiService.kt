package com.santeut.data.apiservice

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.response.MountainListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MountainApiService {

    @GET("/api/mountain/")
    suspend fun searchMountain(
        @Query("name") name: String,
        @Query("region") region: String?
    ): CustomResponse<MountainListResponse>
}