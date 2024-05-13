package com.santeut.data.apiservice

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.request.CreatePostRequest
import com.santeut.data.model.response.PostListResponse
import com.santeut.data.model.response.PostResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PostApiService {

    @GET("/api/community/post")
    suspend fun getPosts(
        @Query("postType") postType: Char
    ): Response<CustomResponse<PostListResponse>>

    @POST("/api/community/post")
    suspend fun createPost(
        @Body createPostRequest: CreatePostRequest
    ): CustomResponse<Unit>

    @GET("/api/community/post/{postId}/{postType}")
    suspend fun readPost(
        @Path("postId") postId: Int,
        @Path("postType") postType: String
    ): CustomResponse<PostResponse>

}