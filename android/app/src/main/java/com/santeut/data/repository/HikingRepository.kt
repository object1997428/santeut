package com.santeut.data.repository

import com.santeut.data.model.request.StartHikingRequest
import com.santeut.data.model.response.StartHikingResponse

interface HikingRepository {

    suspend fun startHiking(startHikingRequest: StartHikingRequest): StartHikingResponse


}