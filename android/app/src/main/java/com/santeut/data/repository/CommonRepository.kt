package com.santeut.data.repository

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.request.CreateCommentRequest
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Path

interface CommonRepository {
    suspend fun createComment(
        postId: Int,
        postType: Char,
        commentRequest: CreateCommentRequest
    ): Flow<Unit>

    suspend fun getNotificationList(): List<NotificationResponse>
<<<<<<< HEAD
=======

    suspend fun hitLike(
        @Path("postId") postId: Int,
        @Path("postType") postType: Char
    ): Flow<Unit>

    suspend fun cancelLike(
        @Path("postId") postId: Int,
        @Path("postType") postType: Char
    ): Flow<Unit>

>>>>>>> 833eccb9f264e9410d5e274d0e6bd350e1a0636c
}