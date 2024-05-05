package com.santeut.domain.usecase

import com.santeut.data.model.request.CreatePostRequest
import com.santeut.data.model.response.PostListResponse
import com.santeut.data.model.response.PostResponse
import com.santeut.data.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend fun getPosts(postType: Char): List<PostResponse> {
        return postRepository.getPosts(postType)
    }

    suspend fun createPost(createPostRequest: CreatePostRequest): Flow<Unit> {
        return postRepository.createPost(createPostRequest)
    }
}