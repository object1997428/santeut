package com.santeut.data.apiservice

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.response.MyProfileResponse
import retrofit2.http.GET

interface UserApiService {

    @GET("/api/auth/user/mypage/profile")
    suspend fun getMyProfile(): CustomResponse<MyProfileResponse>
}