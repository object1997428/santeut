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

<<<<<<< HEAD
    override suspend fun getComments(postId: Int, postType: Char): List<CommentResponse> {
        return try{
            val response = commonApiService.getComments(postId, postType.toString())
            if(response.status=="200"){
                response.data.commentList
            } else{
                Log.e(
                    "CommonRepository",
                    "Error fetching posts: ${response.status} - ${response.data}"
                )
                emptyList()
            }
        } catch (e:Exception){
            Log.e("CommonRepository", "Network error while fetching post: ${e.message}", e)
            throw e
        }
    }
=======
>>>>>>> d6cc9bbc9799cdc6845814115e0a49e707e6e65e
}