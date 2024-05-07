package com.santeut.data.apiservice

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.request.CreateCommentRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path

interface CommonApiService {
    @GET("/api/common/comment/{postId}/{postType}")
    suspend fun readPost(
        @Path("postId") postId: Int,
        @Path("postType") postType: String,
        @Body createCommentRequest: CreateCommentRequest
    ): CustomResponse<Unit>
}