package com.ssafy.santeut.data.apiservice

import retrofit2.Response
import retrofit2.http.GET

interface AuthApiService {
    @GET("/test")
    fun getToken(): Response<String>
}