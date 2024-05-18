package com.santeut.ui.map

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.gson.Gson
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.OverlayImage
import com.santeut.MainApplication
import com.santeut.data.model.request.EndHikingRequest
import com.santeut.data.model.response.LocationDataResponse
import com.santeut.data.model.response.WebSocketMessageResponse
import com.santeut.domain.usecase.HikingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.Point
import org.locationtech.jts.operation.distance.DistanceOp
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val hikingUseCase: HikingUseCase,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : ViewModel() {
    private val _partyId = mutableStateOf(0)
    val partyId = _partyId

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
    val movedDistance = _movedDistance

    // 위치
    private val _myLocation = mutableStateOf<LatLng?>(null)
    val myLocation = _myLocation

    // 고도
    private val _altitude = mutableStateOf(0)
    val altitude = _altitude

    // 최고 고도
    private val _bestHeight = mutableStateOf(0)

    // 유저 위치
    private val _userPositions = mutableStateOf<Map<String, LatLng>>(mapOf())
    val userPositions = _userPositions

    // 유저들 아이콘
    private val _userIcons = mutableStateOf<MutableMap<String, OverlayImage?>>(mutableMapOf())
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
        Log.d("웹소켓 접속", "파티 아이디 : " + _partyId.value.toString())

        val request = Request.Builder()
            .header(
                "Authorization",
                "Bearer ${MainApplication.sharedPreferencesUtil.getAccessToken()}"
            )
            .url(webSocketUrl)
            .build()

        val listener = object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val message = Gson().fromJson(text, WebSocketMessageResponse::class.java)
                    if (message.type == "locationShare") {
                        updateUserState(
                            message.userNickname,
                            message.lat.toDouble(),
                            message.lng.toDouble(),
                            message.userProfile
                        )
                        Log.d(
                            "위치 바듬", message.userNickname + " / " + message.lat
                                    + " / " + message.lng
                        )
                    } else if (message.type == "offCourse") {
                        Log.d("경로 이탈 알림", "${message.userNickname}님이 경로를 이탈하셨습니다.")
                    } else if (message.type == "healthLisk") {
                        Log.d("건강 이상 알림", "${message.userNickname}님이 건강 신호가 좋지 않습니다!")
                    } else if(message.type == "hikingEnd"){
                        Log.d("소모임 종료", "방장이 종료헀어용")
                        endedHiking()
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
                    initData()
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("MapViewModel", "소모임 종료 실패 e: $it") }
                initData()
            }
        }

        stopWebSocket()
    }

    fun initData(){
        _partyId.value = 0
        _distance.value = 0.0
        _courseList.value = emptyList()
        _startTime.value = null
        _stepCount.value = 0
        _movedDistance.value = 0.0
        _altitude.value = 0
        _bestHeight.value = 0
        _userPositions.value = mapOf()
        _userIcons.value = mutableMapOf()
        _deviation.value = 0
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
//                    if (_myLocation.value == null || (_myLocation.value != newLocation && it.accuracy < 30)) {
                        Log.d("위치 변경됨", "${it.latitude} / ${it.longitude} / ${it.altitude}")
                        _myLocation.value = newLocation
                        _altitude.value = it.altitude.toInt()
                        sendLocationUpdate(newLocation)
                        checkRouteDeviation(newLocation)
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
        webSocket?.let { socket ->
//            Log.d("sendLocationUpdate", "위치 업데이트 전송")
            val message = Gson().toJson(
                mapOf(
                    "type" to "locationShare",
                    "lat" to location.latitude,
                    "lng" to location.longitude
                )
            )
            socket.send(message)
        }
    }

    private fun sendMessage(type: String){
        webSocket?.let { socket ->
            val message = Gson().toJson(
                mapOf(
                    "type" to type,
                    "lat" to _myLocation.value?.latitude,
                    "lng" to _myLocation.value?.longitude
                )
            )
            socket.send(message)
        }
    }

    private fun updateUserState(
        userNickname: String,
        lat: Double,
        lng: Double,
        userProfile: String?
    ) {
        val newLatLng = LatLng(lat, lng)
        _userPositions.value = _userPositions.value.toMutableMap().apply {
            this[userNickname] = newLatLng
        }

        viewModelScope.launch {
            val newIcon = userProfile?.let { getUserIcon(it) }
            _userIcons.value = _userIcons.value.toMutableMap().apply {
                this[userNickname] = newIcon
            }
        }
    }

    private suspend fun getUserIcon(url: String): OverlayImage? {
        if (url.isEmpty()) {
            Log.e("ImageDownloadError", "Invalid URL: $url")
            return null
        }

        val bitmap = downloadImage(url)
        return bitmap?.let {
            val circularBitmap = getCircularBitmap(it)
            val resizedBitmap = Bitmap.createScaledBitmap(circularBitmap, 100, 100, false)
            val stream = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            val bitmapResized = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            OverlayImage.fromBitmap(bitmapResized)
        }
    }

    private suspend fun downloadImage(url: String): Bitmap? {
        return try {
            val result = withContext(Dispatchers.IO) {
                val client = OkHttpClient()
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                response.body?.byteStream()?.use {
                    BitmapFactory.decodeStream(it)
                }
            }
            result
        } catch (e: Exception) {
            Log.e("ImageDownloadError", "Error downloading image", e)
            null
        }
    }

    private fun getCircularBitmap(bitmap: Bitmap): Bitmap {
        val size = Math.min(bitmap.width, bitmap.height)
        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(0, 0, size, size)
        val rectF = RectF(rect)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawOval(rectF, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }

    fun checkRouteDeviation(
        latLng: LatLng
    ) {
        val geometryFactory = GeometryFactory()
        val userLocation: Point = geometryFactory.createPoint(Coordinate(latLng.latitude, latLng.longitude))

        if (_courseList.value.isNotEmpty()) {
            val coordinates = _courseList.value.map { Coordinate(it.latitude, it.longitude) }.toTypedArray()
            val predefinedRoute: LineString = geometryFactory.createLineString(coordinates)
            val distanceOp = DistanceOp(predefinedRoute, userLocation)
            val minDistance = distanceOp.distance()
            val distanceInMeters = minDistance * 111319.9

            val isDeviated = distanceInMeters > 20.0
            Log.d("경로 계산", "경로와 떨어진 거리 : $distanceInMeters")

            if (isDeviated) {
                Log.d("경로 이탈", "경로 이탈했어용")
                sendMessage("offCourse")
            }
        } else {
            Log.d("경로 이탈", "경로가 존재하지 않음.")
        }
    }









}

