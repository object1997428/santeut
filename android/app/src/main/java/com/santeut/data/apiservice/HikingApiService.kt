package com.santeut.data.apiservice

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.request.StartHikingRequest
import com.santeut.data.model.response.StartHikingResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface HikingApiService {

    @POST("/api/party/hiking/start")
    fun startHiking(
        @Body hikingRequest: StartHikingRequest
    ): CustomResponse<StartHikingResponse>

}