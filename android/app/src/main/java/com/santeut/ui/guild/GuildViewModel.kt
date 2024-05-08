package com.santeut.ui.guild

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santeut.data.model.response.GuildResponse
import com.santeut.domain.usecase.GuildUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuildViewModel @Inject constructor(
    private val guildUseCase: GuildUseCase
) : ViewModel() {

    private val _guilds = MutableLiveData<List<GuildResponse>>()
    val guilds: LiveData<List<GuildResponse>> = _guilds

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getGuilds() {
        viewModelScope.launch {
            try {
                _guilds.value = guildUseCase.getGuilds()
            } catch (e: Exception) {
                _error.value = "Failed to load posts: ${e.message}"
            }
        }
    }
}