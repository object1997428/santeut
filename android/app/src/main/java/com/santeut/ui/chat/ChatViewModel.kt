package com.santeut.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santeut.data.model.response.ChatMessage
import com.santeut.data.model.response.ChatRoomInfo
import com.santeut.domain.usecase.ChatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor (
    private val chatUseCase: ChatUseCase
) : ViewModel() {

    private val _error= MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _chatrooms = MutableLiveData<List<ChatRoomInfo>>()
    val chatrooms: LiveData<List<ChatRoomInfo>> = _chatrooms

    private val _chatmessages = MutableLiveData<List<ChatMessage>>()
    val chatmessages: LiveData<List<ChatMessage>> = _chatmessages



    fun getChatRoomList() {
        viewModelScope.launch {
            try {
                chatUseCase.getChatRoomList().let {
                    _chatrooms.postValue(chatUseCase.getChatRoomList())
                }
            } catch(e:Exception) {
                _error.value = "Failed to load chat rooms: ${e.message}"
            }
        }
    }

    fun getChatMessageList(partyId: Int) {
        viewModelScope.launch {
            try {
                chatUseCase.getChatMessageList(partyId).let {
                    _chatmessages.postValue(chatUseCase.getChatMessageList(partyId))
                }
            } catch(e: Exception) {
                _error.value = "Failed to load chat messages(party: ${partyId}): ${e.message}}"
            }
        }
    }

}
