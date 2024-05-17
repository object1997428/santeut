package com.santeut.data.apiservice

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.request.EndHikingRequest
import com.santeut.data.model.request.StartHikingRequest
import com.santeut.data.model.response.StartHikingResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface HikingApiService {

    @POST("/api/party/hiking/start")
    fun startHiking(
        @Body startHikingRequest: StartHikingRequest
    ): CustomResponse<StartHikingResponse>

    @POST("/api/party/hiking/end")
    fun endHiking(
        @Body endHikingRequest: EndHikingRequest
    ): CustomResponse<Unit>

}