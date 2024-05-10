@file:OptIn(ExperimentalNaverMapApi::class)

package com.example.testmap

import android.content.Context
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.core.content.ContextCompat.getSystemService
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.compose.material3.Button
import kotlinx.coroutines.delay


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

class MainActivity : ComponentActivity() {
    companion object {
        private const val REQUEST_CODE_ACTIVITY_RECOGNITION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), REQUEST_CODE_ACTIVITY_RECOGNITION)
        }

        setContent {
            TestmapTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MapScreen(context = this@MainActivity, token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MDAxIiwiaWF0IjoxNzE0NzA2MTEwLCJleHAiOjE3NDYyMzYxMTB9.c0bRrGh0NGA8HPd_oCMCUPfmQaAwzswftvHdv8xulzg")
                }
            }
        }
    }
}
@ExperimentalNaverMapApi
@Composable
fun MapScreen(context: Context, token: String) {
    val mountainService = remember { createMountainService(token) }
    var mountainData by remember { mutableStateOf<MountainData?>(null) }
    var stepCount by remember { mutableIntStateOf(0) }
    var distanceMoved by remember { mutableFloatStateOf(0f) }
    var timerRunning by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableStateOf(0) } // 초 단위로 경과 시간을 저장합니다.
    val scope = rememberCoroutineScope()

    // 센서 관리자와 걸음 수 센서 초기화
    val sensorManager = getSystemService(context, SensorManager::class.java)
    val stepDetectorSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

    // 센서 리스너 등록
    DisposableEffect(Unit) {
        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
                    stepCount += event.values[0].toInt()
                    Log.d("StepDetector", "Step detected: Total steps = $stepCount")
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

    LaunchedEffect(key1 = true) {
        mountainData = withContext(Dispatchers.IO) {
            try {
                val response = mountainService.getMountainData()
                response.data
            } catch (e: Exception) {
                Log.e("MapScreen", "Error fetching mountain data", e)
                null
            }
        }
    }

    // 타이머 로직
    if (timerRunning) {
        LaunchedEffect(key1 = timerRunning) {
            while (timerRunning) {
                delay(1000) // 1초 대기
                elapsedTime += 1 // 경과 시간 1초 증가
            }
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(LatLng(35.8447943443487, 127.11199020254), 16.0)
    }

    LaunchedEffect(key1 = mountainData) {
        mountainData?.let {
            cameraPositionState.position = CameraPosition(LatLng(it.lat, it.lng), 16.0)
        }
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
            }
            // "등산 시작" 및 "중지" 버튼
            Button(
                onClick = {
                    if (timerRunning) {
                        timerRunning = false
                        elapsedTime = 0 // 타이머가 중지될 때 경과 시간을 초기화합니다.
                    } else {
                        timerRunning = true
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
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
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}
