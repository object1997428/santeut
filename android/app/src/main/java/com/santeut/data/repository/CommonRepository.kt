package com.santeut.data.repository

import com.santeut.data.model.request.CreateCommentRequest
import kotlinx.coroutines.flow.Flow

interface CommonRepository {
    suspend fun createComment(
        postId: Int,
        postType: Char,
        commentRequest: CreateCommentRequest
    ): Flow<Unit>
}