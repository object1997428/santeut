package com.santeut.ui.community

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santeut.data.model.request.CreateCommentRequest
import com.santeut.data.model.request.CreatePostRequest
import com.santeut.data.model.response.CommentListResponse
import com.santeut.data.model.response.CommentResponse
import com.santeut.data.model.response.PostListResponse
import com.santeut.domain.usecase.CommonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(
    private val commonUseCase: CommonUseCase
) : ViewModel() {

    private val _comments = MutableLiveData<List<CommentResponse>>()
    val comments: LiveData<List<CommentResponse>> = _comments

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun createComment(postId: Int, postType: Char, commentContent: String) {
        viewModelScope.launch {
            try {
                val createCommentRequest = CreateCommentRequest(commentContent)
                commonUseCase.createComment(postId, postType, createCommentRequest).collect {
                    Log.d("CommonViewModel", "Create Comment")
                }
            } catch (e: Exception) {
                _error.value = "Failed to load posts: ${e.message}"
            }
        }
    }

    fun getComment(postId: Int, postType: Char){
        viewModelScope.launch {
            try{
                _comments.value = commonUseCase.getComments(postId, postType)
            } catch (e:Exception){
                _error.value = "Failed to load posts: ${e.message}"

            }
        }
    }


}