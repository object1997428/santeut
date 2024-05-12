package com.santeut.ui.guild

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santeut.data.model.request.CreateGuildPostRequest
import com.santeut.data.model.response.GuildPostResponse
import com.santeut.data.model.response.GuildResponse
import com.santeut.domain.usecase.GuildUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class GuildViewModel @Inject constructor(
    private val guildUseCase: GuildUseCase
) : ViewModel() {

    private val _guilds = MutableLiveData<List<GuildResponse>>(emptyList())
    val guilds: LiveData<List<GuildResponse>> = _guilds

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _guild = MutableLiveData<GuildResponse>()
    val guild: LiveData<GuildResponse> = _guild

    private val _postList = MutableLiveData<List<GuildPostResponse>>(emptyList())
    val postList: LiveData<List<GuildPostResponse>> = _postList

    fun getGuilds() {
        viewModelScope.launch {
            try {
                _guilds.postValue(guildUseCase.getGuilds())
            } catch (e: Exception) {
                _error.postValue("동호회 목록 조회 실패: ${e.message}")
            }
        }
    }

    fun myGuilds() {
        viewModelScope.launch {
            try {
                _guilds.postValue(guildUseCase.myGuilds())
            } catch (e: Exception) {
                _error.postValue("내 동호회 목록 조회 실패: ${e.message}")
            }
        }
    }

    fun getGuild(guildId: Int) {
        viewModelScope.launch {
            try {
                _guild.value = guildUseCase.getGuild(guildId)
            } catch (e: Exception) {
                _error.postValue("동호회 정보 조회 실패: ${e.message}")
            }
        }
    }

    fun applyGuild(guildId: Int) {
        viewModelScope.launch {
            try {
                guildUseCase.applyGuild(guildId).collect {
                    Log.d("GuildViewModel", "가입 요청 전송")
                }
            } catch (e: Exception) {
                _error.postValue("가입 요청 전송 실패: ${e.message}")
            }
        }
    }

    fun getGuildPostList(guildId: Int, categoryId: Int) {
        viewModelScope.launch {
            try {
                _postList.postValue(guildUseCase.getGuildPostList(guildId, categoryId))
            } catch (e: Exception) {
                _error.postValue("동호회 게시글 목록 조회 실패: ${e.message}")
            }
        }
    }

    fun createGuildPost(
        images: List<MultipartBody.Part>,
        createGuildPostRequest: CreateGuildPostRequest
    ) {
        viewModelScope.launch {
            try {
                Log.d("ViewModel", "접근 성공")
                guildUseCase.createGuildPost(images, createGuildPostRequest).collect {
                    Log.d("GuildViewModel", "동호회 게시글 작성 성공")
                }
            } catch (e: Exception) {
                _error.postValue("동호회 게시글 작성 실패: ${e.message}")
            }
        }
    }
}