package com.santeut.data.apiservice

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.request.LoginRequest
import com.santeut.data.model.request.SignUpRequest
import com.santeut.data.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {
    @GET("/test")
    suspend fun getToken(): Response<String>
    @POST("/api/auth/signin")
    suspend fun login(@Body loginRequest: LoginRequest): CustomResponse<LoginResponse>
    @POST("/api/auth/signup")
    suspend fun signup(@Body signUpRequest: SignUpRequest): CustomResponse<Unit>
}