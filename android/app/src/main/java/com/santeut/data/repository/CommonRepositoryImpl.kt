package com.santeut.data.repository

import com.santeut.data.apiservice.CommonApiService
import com.santeut.data.model.request.CreateCommentRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CommonRepositoryImpl @Inject constructor(
    private val commonApiService: CommonApiService
) : CommonRepository {

    override suspend fun createComment(
        postId: Int,
        postType: Char,
        commentRequest: CreateCommentRequest
    ): Flow<Unit> = flow {
        val response = commonApiService.createComment(postId, postType.toString(), commentRequest)
        if (response.status == "201") {
            emit(response.data)
        }
    }

}