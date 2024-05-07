package com.santeut.ui.community

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santeut.data.model.request.CreateCommentRequest
import com.santeut.data.model.request.CreatePostRequest
import com.santeut.domain.usecase.CommonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(
    private val commonUseCase: CommonUseCase
) : ViewModel() {

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
}