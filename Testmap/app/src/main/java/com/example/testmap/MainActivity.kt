package com.example.testmap

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.testmap.ui.theme.TestmapTheme
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import okhttp3.*
import com.google.gson.Gson
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.compose.material3.Button
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.tasks.await

// API 인터페이스 정의
interface MountainService {
    @GET("mountain/v2/2")
    suspend fun getMountainData(): MountainResponse
}

// 데이터 클래스 정의
data class MountainResponse(
    val status: Int,
    val data: MountainData
)

data class MountainData(
    val mountainName: String,
    val address: String,
    val description: String,
    val height: Int,
    val courseCount: Int,
    val lat: Double,
    val lng: Double,
    val views: Int,
    val image: String?
)

// 인증 토큰을 추가하는 Interceptor 구현
class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}

// OkHttpClient 및 Retrofit 서비스 설정
fun createMountainService(token: String): MountainService {
    val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(token))
        .build()

    return Retrofit.Builder()
        .baseUrl("https://k10e201.p.ssafy.io/api/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MountainService::class.java)
}

@ExperimentalNaverMapApi
class MainActivity : ComponentActivity() {
    private var webSocket: WebSocket? = null
    private val webSocketUrl = "ws://k10e201.p.ssafy.io:52715/api/hiking/chat/rooms/1"

    companion object {
        private const val REQUEST_CODE_ACTIVITY_RECOGNITION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestmapTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MapScreen(context = this@MainActivity, token = "your_token_here", ::startWebSocket, ::stopWebSocket)
                }
            }
        }
    }

    fun startWebSocket(updateLocation: (Double, Double) -> Unit, context: Context) {
        val request = Request.Builder().url(webSocketUrl).build()
        var locationSendingJob: Job? = null  // 위치 전송 코루틴 작업을 참조하기 위한 변수

        val listener = object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "Received message: $text")
                try {
                    val message = Gson().fromJson(text, WebSocketMessage::class.java)
                    updateLocation(message.lat.toDouble(), message.lng.toDouble())
                } catch (e: Exception) {
                    Log.e("WebSocket", "Error parsing message: ${e.localizedMessage}", e)
                }
            }

            override fun onOpen(webSocket: WebSocket, response: Response) {
                // 웹소켓 연결이 성공하면 초기 메시지를 보내고 위치 정보를 주기적으로 보내기 시작
                webSocket.send("{\"hello\"}")
                Log.d("WebSocket", "WebSocket opened and initial message sent.")

                locationSendingJob = CoroutineScope(Dispatchers.IO).launch {
                    while (isActive) {
                        delay(5000)  // 5초마다 반복
                        val currentLocation = getCurrentLocation(context)
                        currentLocation?.let {
                            val locationJson = Gson().toJson(WebSocketMessage(
                                type = "message",
                                partyId = 1,
                                userId = 3,
                                userNickname = "하이",
                                lat = it.latitude.toString(),
                                lng = it.longitude.toString()
                            ))
                            webSocket.send(locationJson)
                            Log.d("WebSocket", "Location sent: $locationJson")
                        }
                    }
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WebSocket", "WebSocket is closing: $reason")
                locationSendingJob?.cancel()  // 위치 전송 코루틴 작업 취소
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "WebSocket connection failure: ${t.localizedMessage}", t)
                locationSendingJob?.cancel()  // 실패 시 위치 전송 코루틴 작업 취소
            }
        }

        webSocket = OkHttpClient().newWebSocket(request, listener)
    }





    fun stopWebSocket() {
        webSocket?.close(1000, "Activity Ended")
        webSocket = null
    }
}

@ExperimentalNaverMapApi
@Composable
fun MapScreen(
    context: Context,
    token: String,
    startWebSocket: (updateLocation: (Double, Double) -> Unit, Context) -> Unit,
    stopWebSocket: () -> Unit
) {
    val mountainService = remember { createMountainService(token) }
    var mountainData by remember { mutableStateOf<MountainData?>(null) }
    var otherUserPosition by remember { mutableStateOf<LatLng?>(null) }

    var stepCount by remember { mutableStateOf(0) }
    var distanceMoved by remember { mutableStateOf(0f) }
    var timerRunning by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableStateOf(0) }

    // 사용자 위치 마커 상태 관리
    val otherUserMarkerState = rememberMarkerState()
    LaunchedEffect(otherUserPosition) {
        otherUserPosition?.let {
            otherUserMarkerState.position = it
        }
    }

    val sensorManager = getSystemService(context, SensorManager::class.java)
    val stepDetectorSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

    DisposableEffect(Unit) {
        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
                    stepCount += event.values[0].toInt()
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        stepDetectorSensor?.let {
            sensorManager?.registerListener(sensorEventListener, it, SensorManager.SENSOR_DELAY_UI)
        }

        onDispose {
            sensorManager?.unregisterListener(sensorEventListener)
        }
    }

    LaunchedEffect(timerRunning) {
        if (timerRunning) {
            startWebSocket({ lat, lng ->
                otherUserPosition = LatLng(lat, lng)
            }, context)
            while (timerRunning) {
                delay(1000)
                elapsedTime += 1
            }
        } else {
            stopWebSocket()
            elapsedTime = 0
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(LatLng(35.8447943443487, 127.11199020254), 16.0)
    }

    val uiSettings = remember {
        MapUiSettings(
            isZoomControlEnabled = true,
            isLocationButtonEnabled = true,
            isCompassEnabled = true
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(8f)) {
            NaverMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                locationSource = rememberFusedLocationSource(isCompassEnabled = uiSettings.isCompassEnabled),
                properties = MapProperties(
                    locationTrackingMode = LocationTrackingMode.Follow,
                    mapType = MapType.Terrain,
                    isMountainLayerGroupEnabled = true
                ),
                uiSettings = uiSettings
            ) {
                mountainData?.let {
                    Marker(
                        state = rememberMarkerState(position = LatLng(it.lat, it.lng)),
                        captionText = it.mountainName,
                        captionTextSize = 14.sp,
                        captionMinZoom = 12.0
                    )
                }
                Marker(
                    state = otherUserMarkerState,
                    captionText = "다른 등산객",
                    captionTextSize = 14.sp,
                    captionMinZoom = 12.0
                )
            }

            Button(
                onClick = {
                    timerRunning = !timerRunning
                },
                modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
            ) {
                Text(if (timerRunning) "중지" else "등산 시작")
            }
        }
        Box(modifier = Modifier.weight(2f)) {
            InfoPanel(mountainData?.height ?: 0, stepCount, distanceMoved, elapsedTime)
        }
    }
}


@Composable
fun InfoPanel(elevation: Int, steps: Int, distance: Float, timeInSeconds: Int) {
    val hours = timeInSeconds / 3600
    val minutes = (timeInSeconds % 3600) / 60
    val seconds = timeInSeconds % 60

    Surface(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            InfoItem(title = "고도", value = "$elevation m")
            InfoItem(title = "걸음 수", value = "$steps 걸음")
            InfoItem(title = "움직인 거리", value = String.format("%.2f km", distance))
            InfoItem(title = "경과 시간", value = String.format("%02d시 %02d분 %02d초", hours, minutes, seconds))
        }
    }
}

@Composable
fun InfoItem(title: String, value: String) {
    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}

data class WebSocketMessage(
    val type: String,
    val partyId: Int,
    val userId : Int,
    val userNickname: String,
    val lat: String,
    val lng: String
)

data class LocationData(
    val latitude: Double,
    val longitude: Double
)

// 위치 정보를 비동기적으로 가져오는 함수
suspend fun getCurrentLocation(context: Context): LocationData? {
    val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // 권한 요청 로직 필요
        return null
    }

    return try {
        val location = fusedLocationProviderClient.lastLocation.await()
        if (location != null) {
            LocationData(location.latitude, location.longitude)
        } else {
            null
        }
    } catch (e: Exception) {
        Log.e("LocationError", "Error getting location", e)
        null
    }
}
