package com.santeut.ui.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.santeut.MainApplication
import com.santeut.data.model.response.ChatMessage
import com.santeut.data.model.response.ChatRoomInfo
import com.santeut.domain.usecase.ChatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor (
    private val chatUseCase: ChatUseCase
) : ViewModel() {

    private val _error= MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _chatrooms = MutableLiveData<List<ChatRoomInfo>>()
    val chatrooms: LiveData<List<ChatRoomInfo>> = _chatrooms

//    private val _chatmessages = MutableLiveData<MutableList<ChatMessage>>()
    private val _chatmessages = MutableLiveData<MutableList<ChatMessage>>()
    val chatmessages: LiveData<MutableList<ChatMessage>> = _chatmessages
//    val chatmessages = _chatmessages.value ?: mutableListOf()

//    private val chatService = ChatService()
////    val socketStatus = chatService.isConnected



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
                    _chatmessages.postValue(chatUseCase.getChatMessageList(partyId).toMutableList())
                }
            } catch(e: Exception) {
                _error.value = "Failed to load chat messages(party: ${partyId}): ${e.message}}"
            }
        }
    }

    // web socket
    private val okHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null

    private val _isConnected = MutableStateFlow(false)
    val isConnected = _isConnected.asStateFlow()

    private val webSocketListener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d("웹소켓 리스너", "onOpen")
            _isConnected.value = true
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("웹소켓 리스너", "onMessage ${text}")
            var json: JSONObject? = null
            json = JSONObject(text)

            Log.d("SENDER: ", "${json.get("userId")}번 유저: ${json.get("userNickname").toString()}, ${json.get("userProfile").toString()}, ${json.get("content").toString()}")
            var msg = ChatMessage(
                json.get("createdAt").toString(),
                json.get("userNickname").toString(),
                json.get("userProfile").toString(),
                json.get("content").toString()
            )
//            addChatMessage(msg) // 안됨
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("웹소켓 리스너", "onClosing")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("웹소켓 리스너", "onClosed")
            _isConnected.value = false
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.d("웹소켓 리스너", "onFailure")
            Log.d("onFailure", "${webSocket.toString()}/ $t/ $response")
        }
    }

    fun connect(partyId: Int) {
        val webSocketUrl =
            "wss://k10e201.p.ssafy.io/api/party/ws/chat/room/${partyId}"

        val token = MainApplication.sharedPreferencesUtil.getAccessToken()

        val request:Request = Request.Builder()
            .header("Authorization", "Bearer ${token}")
            .url(webSocketUrl)
            .build()
        webSocket = okHttpClient.newWebSocket(request, webSocketListener)
    }

    fun disconnect() {
        webSocket?.close(1000, "Disconnected by client")
    }

    fun shutdown() {
        okHttpClient.dispatcher.executorService.shutdown()
    }

    fun sendMessage(message: Message) {
        Log.d("sendMessage","${_isConnected.value}")
        if (_isConnected.value) {
            var gson = Gson()
            var text = gson.toJson(message)
            webSocket?.send(text)
            if(webSocket != null) {
                Log.d("sendMessage", text);
            }
        }
    }

    fun addChatMessage(message: ChatMessage) {
//        chatmessages.add(message)
//        _chatmessages.value = chatmessages

//        var tempList = _chatmessages.value
//        Log.d("addChatMessage", tempList.toString())
//        if (tempList != null) {
//            Log.d("tempList","not null")
//            chatmessages.value?.let { tempList.addAll(it) }
//        }
//        _chatmessages.value = tempList?: mutableListOf()

        _chatmessages.value?.add(message)
        _chatmessages.value = _chatmessages.value

    }

}
