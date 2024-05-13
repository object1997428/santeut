package com.santeut.data.repository

import com.santeut.data.model.request.CreateCommentRequest
import com.santeut.data.model.response.CommentListResponse
import com.santeut.data.model.response.CommentResponse
import kotlinx.coroutines.flow.Flow

interface CommonRepository {
    suspend fun createComment(
        postId: Int,
        postType: Char,
        commentRequest: CreateCommentRequest
    ): Flow<Unit>

    suspend fun getComments(postId: Int, postType: Char): List<CommentResponse>
}