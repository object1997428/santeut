package com.santeut.data.apiservice

import android.telecom.Call
import com.santeut.data.model.request.PlantIdentificationRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PlantIdApi {
//    @Headers("Content-Type: application/json")
    @Headers("api-key: GznA9rJgHH4y61et2aOkIOc112CkaYa9prbrGe94BBtXItzWHO")
    @POST("v3/identification?details=description")
    suspend fun identifyPlant(@Body request: PlantIdentificationRequest): Response<ResponseBody>

}