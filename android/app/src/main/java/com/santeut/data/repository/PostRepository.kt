package com.santeut.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.santeut.data.model.request.CreatePostRequest
import com.santeut.data.model.response.PostResponse

interface PostRepository {

    suspend fun getPosts(postType: Char): List<PostResponse>
    suspend fun createPost(createPostRequest: CreatePostRequest): Flow<Unit>

    suspend fun readPost(postId: Int, postType: Char): PostResponse
}