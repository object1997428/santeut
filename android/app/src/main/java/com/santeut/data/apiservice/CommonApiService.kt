package com.santeut.data.apiservice

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.request.CreateCommentRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface CommonApiService {
    @POST("/api/common/comment/{postId}/{postType}")
    suspend fun createComment(
        @Path("postId") postId: Int,
        @Path("postType") postType: String,
        @Body createCommentRequest: CreateCommentRequest
    ): CustomResponse<Unit>

}