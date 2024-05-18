package com.santeut.ui.map

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.tubesock.WebSocketMessage
import com.google.gson.Gson
import com.santeut.MainApplication
import com.santeut.data.model.request.EndHikingRequest
import com.santeut.data.model.request.StartHikingRequest
import com.santeut.data.model.response.CourseDetailResponse
import com.santeut.data.model.response.LocationDataResponse
import com.santeut.data.model.response.WebSocketMessageResponse
import com.santeut.domain.usecase.HikingUseCase
import com.santeut.domain.usecase.PartyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

@HiltViewModel
class HikingViewModel @Inject constructor(
    private val partyUseCase: PartyUseCase,
    private val hikingUseCase: HikingUseCase
) : ViewModel() {
    var webSocket: WebSocket? = null

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _distance = MutableLiveData<Double>()
    val distance: LiveData<Double> = _distance

    private val _courseList = MutableLiveData<List<LocationDataResponse>>()
    val courseList: LiveData<List<LocationDataResponse>> = _courseList

    fun startHiking(startHikingRequest: StartHikingRequest) {
        viewModelScope.launch {
            try {
                _distance.postValue(hikingUseCase.startHiking(startHikingRequest).distance)
                _courseList.postValue(hikingUseCase.startHiking(startHikingRequest).courseList)
            } catch (e: Exception) {
                _error.postValue("소임 시작 실패: ${e.message}")
            }
        }
    }

    fun endHiking(endHikingRequest: EndHikingRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                hikingUseCase.endHiking(endHikingRequest).collect {
                    Log.d("HikingViewModel", "소모임 종료 성공")
                }
            } catch (e: Exception) {
                _error.postValue("소모임 종료 실패: ${e.message}")
            }
        }
    }

    fun startWebSocket(
        partyId: Int,
        updateLocation: (String, Double, Double, String?) -> Unit,
        showAlert: MutableState<Boolean>,
        alertMessage: MutableState<String>
    ) {
        // Web Socket
        val webSocketUrl = "wss://k10e201.p.ssafy.io/api/hiking/chat/rooms/${partyId}"

        val request = Request.Builder()
            .header(
                "Authorization",
                "Bearer ${MainApplication.sharedPreferencesUtil.getAccessToken()}"
            )
            .url(webSocketUrl)
            .build()

        val listener = object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "Received message: $text")
                try {
                    val message = Gson().fromJson(text, WebSocketMessageResponse::class.java)
                    updateLocation(
                        message.userNickname,
                        message.lat.toDouble(),
                        message.lng.toDouble(),
                        message.userProfile
                    )

                    if (message.type == "offCourse") {
                        showAlert.value = true
                        alertMessage.value = "${message.userNickname}님이 경로를 이탈하셨습니다."
                    }
                } catch (e: Exception) {
                    Log.e("WebSocket", "Error parsing message: ${e.localizedMessage}", e)
                }
            }

            override fun onOpen(webSocket: WebSocket, response: Response) {
                webSocket.send("{\"type\": \"enter\"}")
                Log.d("WebSocket", "WebSocket opened and initial message sent.")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WebSocket", "WebSocket is closing: $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "WebSocket connection failure: ${t.localizedMessage}", t)
            }
        }

        webSocket = OkHttpClient().newWebSocket(request, listener)
    }

    fun stopWebSocket() {
        webSocket?.send("{\"type\": \"quit\"}")
        webSocket?.close(1000, "Activity Ended")
        webSocket = null
    }
}