package com.santeut.domain.usecase

import com.santeut.data.model.request.CreateCommentRequest
import com.santeut.data.model.response.NotificationResponse
import com.santeut.data.repository.CommonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommonUseCase @Inject constructor(
    private val commonRepository: CommonRepository
) {
    suspend fun createComment(
        postId: Int,
        postType: Char,
        createCommentRequest: CreateCommentRequest
    ): Flow<Unit> =
        commonRepository.createComment(postId, postType, createCommentRequest)

    suspend fun getNotificationList(): List<NotificationResponse> =
        commonRepository.getNotificationList()
}