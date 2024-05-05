package com.santeut.data.repository

import androidx.lifecycle.LiveData
import com.santeut.data.model.response.PostResponse

interface PostRepository{

    suspend fun getPosts(postType: Char): List<PostResponse>
}