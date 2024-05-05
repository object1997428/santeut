package com.santeut.ui.community

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santeut.data.apiservice.PostApiService
import com.santeut.data.model.request.CreatePostRequest
import com.santeut.data.model.response.PostListResponse
import com.santeut.data.model.response.PostResponse
import com.santeut.domain.usecase.PostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postUseCase: PostUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _posts = MutableLiveData<List<PostResponse>>()
    val posts: LiveData<List<PostResponse>> = _posts

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _postCreationSuccess = MutableLiveData<Boolean>()
    val postCreationSuccess: LiveData<Boolean> = _postCreationSuccess

    init {
        val postType = savedStateHandle.get<String>("postType")?.first() ?: 'T'
        getPosts(postType)
    }

    private fun getPosts(postType: Char) {
        viewModelScope.launch {
            try {
                _posts.value = postUseCase.getPosts(postType)
            } catch (e: Exception) {
                _error.value = "Failed to load posts: ${e.message}"
            }
        }
    }

    fun createPost(postTitle: String, postContent: String, postType: Char, userPartyId: Int) {
        viewModelScope.launch {
            try {
                val createPostRequest =
                    CreatePostRequest(postTitle, postContent, postType, userPartyId)
                postUseCase.createPost(createPostRequest).collect {
                    _postCreationSuccess.value = true
                }
            } catch (e: Exception) {
                _error.value = "Failed to load posts: ${e.message}"
                _postCreationSuccess.value = false
            }
        }
    }
}