package com.ssafy.santeut.data.apiservice

import com.ssafy.santeut.data.model.BaseResponse
import com.ssafy.santeut.data.model.request.LoginRequest
import com.ssafy.santeut.data.model.request.SignUpRequest
import com.ssafy.santeut.data.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {
    @GET("/test")
    suspend fun getToken(): Response<String>
    @POST("/api/auth/signin")
    suspend fun login(@Body loginRequest: LoginRequest): BaseResponse<LoginResponse>
    @POST("/api/auth/signup")
    suspend fun signup(@Body signUpRequest: SignUpRequest): BaseResponse<Unit>
}