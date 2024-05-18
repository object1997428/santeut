package com.santeut.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.MarkerState
import com.santeut.MainApplication
import com.santeut.data.model.request.EndHikingRequest
import com.santeut.data.model.response.LocationDataResponse
import com.santeut.data.model.response.UserLocationDataResponse
import com.santeut.data.model.response.WebSocketMessageResponse
import com.santeut.domain.usecase.HikingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val hikingUseCase: HikingUseCase,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : ViewModel() {
    private val _partyId = mutableStateOf(0)

    // 웹 소켓
    var webSocket: WebSocket? = null

    // 등산 코스 길이
    private val _distance = mutableStateOf(0.0)
    val distance = _distance

    // 등산 코스 리스트
    private val _courseList = mutableStateOf<List<LatLng>>(emptyList())
    val courseList = _courseList

    // 등산 시간
    private val _startTime = mutableStateOf<LocalDateTime?>(null)
    val startTime = _startTime

    // 걸음 수
    private val _stepCount = mutableStateOf(0)
    val stepCount = _stepCount

    // 이동 거리
    private val _movedDistance = mutableStateOf(0.0)
    val movedDistance = _stepCount

    // 위치
    private val _myLocation = mutableStateOf<LatLng?>(null)
    val myLocation = _stepCount

    // 고도
    private val _altitude = mutableStateOf(0)
    val altitude = _altitude

    // 최고 고도
    private val _bestHeight = mutableStateOf(0)

    // 유저 위치
    private val _userPositions = mutableStateOf<Map<String, LatLng>>(mapOf())
    val userPositions = _userPositions

    // 유저들 아이콘
    private val _userIcons = mutableStateOf<MutableMap<String, MarkerState>>(mutableMapOf())
    val userIcons = _userIcons

    // 경로 이탈 계산
    private val _deviation = mutableStateOf(0)
    val deviation = _deviation

    // 알림 처리
    // 나중에

    // 여기에 하면 안되는데...
    init {
        startLocationUpdates()
    }

    fun startHiking() {
        _startTime.value = LocalDateTime.now()

        // 웹소켓 접속
        val webSocketUrl = "wss://k10e201.p.ssafy.io/api/hiking/chat/rooms/${_partyId.value}"

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
//                    updateLocation(
//                        message.userNickname,
//                        message.lat.toDouble(),
//                        message.lng.toDouble(),
//                        message.userProfile
//                    )
                    if (message.type == "locationShare"){
                        Log.d("위치 바등ㅁ", message.userNickname + " / " + message.lat
                        + " / " + message.lng)
                    }
                    else if (message.type == "offCourse") {
                        Log.d("경로 이탈 알림", "${message.userNickname}님이 경로를 이탈하셨습니다.")
                    }
                } catch (e: Exception) {
                    Log.e("WebSocket", "Error parsing message: ${e.localizedMessage}", e)
                }
            }

            override fun onOpen(webSocket: WebSocket, response: Response) {
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

    fun endedHiking() {
        // back server 로 종료 요청
        viewModelScope.launch {
            try {
                hikingUseCase.endHiking(
                    EndHikingRequest(
                        _partyId.value,
                        _distance.value.toInt(),
                        _bestHeight.value,
                        LocalDateTime.now()
                    )
                ).collect {
                    Log.d("MapViewModel", "소모임 종료 성공")
                    _partyId.value = 0
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("MapViewModel", "소모임 종료 실패 e: $it") }
                _partyId.value = 0
            }
        }

        stopWebSocket()
    }

    fun stopWebSocket() {
        webSocket?.close(1000, "Activity Ended")
        webSocket = null
    }

    // party screen 에서 호출 -> 하이킹 시작 메서드임.
    fun setHikingData(
        pId: Int,
        dist: Double,
        course: List<LocationDataResponse>
    ) {
        _partyId.value = pId
        _distance.value = dist
        _courseList.value = course.map { LatLng(it.lat, it.lng) }

        startHiking()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(5000L)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result ?: return
                result.locations.lastOrNull()?.let {
                    val newLocation = LatLng(it.latitude, it.longitude)
                    Log.d("위치 업데이트", "${it.latitude} / ${it.longitude}")
//                    if (_myLocation.value == null || (_myLocation.value != newLocation && it.accuracy < 30)) {
                        _myLocation.value = newLocation
                        sendLocationUpdate(newLocation)
//                    }
                }
            }
        }

        viewModelScope.launch {
            try {
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    null
                )
            } catch (e: SecurityException) {
                e.message?.let { Log.e("startLocationUpdates", it) }
            }
        }
    }

    private fun sendLocationUpdate(location: LatLng) {
        Log.d("sendLocationUpdate", "위치 업데이트 전송")
        webSocket?.let { socket ->
            val message = Gson().toJson(mapOf(
                "type" to "locationShare",
                "lat" to location.latitude,
                "lng" to location.longitude
            ))
            socket.send(message)
        }
    }
}

