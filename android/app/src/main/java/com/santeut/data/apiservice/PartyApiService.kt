package com.santeut.data.apiservice

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.response.PartyListResponse
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate

interface PartyApiService {

    @GET("/api/party")
    suspend fun getParties(
        @Query("guild") guild: Int,
        @Query("name") name: String,
        @Query("start") start: LocalDate,
        @Query("end") end: LocalDate
    ): CustomResponse<PartyListResponse>
}