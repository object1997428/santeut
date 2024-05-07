package com.santeut.data.repository

import android.util.Log
import com.santeut.data.apiservice.CommonApiService
import com.santeut.data.model.request.CreateCommentRequest
import com.santeut.data.model.response.CommentListResponse
import com.santeut.data.model.response.CommentResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
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

    override suspend fun getComments(postId: Int, postType: Char): List<CommentResponse> {
        try{
            val response = commonApiService.getComments(postId, postType.toString())
            if(response.status=="200"){

            } else{
                throw Exception("Error")
            }
        } catch (e:Exception){
            Log.e("CommonRepository", "Network error while fetching post: ${e.message}", e)
            throw e
        }
    }
}