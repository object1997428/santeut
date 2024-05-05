package com.santeut.data.apiservice

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.response.PostListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PostApiService {

    @GET("/api/community/post")
    suspend fun getPosts(
        @Query("postType") postType: Char
    ): Response<CustomResponse<PostListResponse>>


}