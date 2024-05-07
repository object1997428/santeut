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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

//@Composable
//fun MapScreen(context: Context, token: String) {
//    val mountainService = remember { createMountainService(token) }
//    var mountainData by remember { mutableStateOf<MountainData?>(null) }
//
//    LaunchedEffect(key1 = true) {
//        mountainData = withContext(Dispatchers.IO) {
//            try {
//                val response = mountainService.getMountainData()
//                Log.d("MapScreen", "API Response: $response")  // 로그로 API 응답 출력
//                if (response.data != null) {
//                    Log.d("MapScreen", "Mountain Lat: ${response.data.lat}, Lng: ${response.data.lng}")
//                }
//                response.data
//            } catch (e: Exception) {
//                Log.e("MapScreen", "Error fetching mountain data", e)  // 에러 로깅
//                null
//            }
//        }
//    }
//
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition(LatLng(35.8447943443487, 127.11199020254), 14.0)
//    }
//
//    LaunchedEffect(key1 = mountainData) {
//        mountainData?.let {
//            cameraPositionState.position = CameraPosition(LatLng(it.lng, it.lat), 14.0)
//        }
//    }
//
//    val uiSettings = remember {
//        MapUiSettings(
//            isZoomControlEnabled = true,
//            isLocationButtonEnabled = true,
//            isCompassEnabled = true
//        )
//    }
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        Box(modifier = Modifier.weight(1f)) {
//            NaverMap(
//                modifier = Modifier.fillMaxSize(),
//                cameraPositionState = cameraPositionState,
//                locationSource = rememberFusedLocationSource(isCompassEnabled = uiSettings.isCompassEnabled),
//                properties = MapProperties(
//                    locationTrackingMode = LocationTrackingMode.Follow,
//                    mapType = MapType.Terrain,
//                    isMountainLayerGroupEnabled = true
//                ),
//                uiSettings = uiSettings
//            ) {
//                mountainData?.let {
//                    Marker(
//                        state = rememberMarkerState(position = LatLng(it.lng, it.lat)),
//                        captionText = it.mountainName,
//                        captionTextSize = 14.sp,
//                        captionMinZoom = 12.0
//                    )
//                }
//            }
//        }
//    }
//}

@ExperimentalNaverMapApi
@Composable
fun MapScreen(context: Context, token: String) {
    val mountainService = remember { createMountainService(token) }
    var mountainData by remember { mutableStateOf<MountainData?>(null)}
    var stepCount by remember { mutableIntStateOf(0) }  // 정수형 데이터 관리를 위한 mutableIntStateOf 사용
    var distanceMoved by remember { mutableFloatStateOf(0f) }  // 부동 소수점 데이터 관리를 위한 mutableFloatStateOf 사용

    LaunchedEffect(key1 = true) {
        mountainData = withContext(Dispatchers.IO) {
            try {
                val response = mountainService.getMountainData()
                Log.d("MapScreen", "API Response: $response")
                if (response.data != null) {
                    Log.d("MapScreen", "Mountain Lat: ${response.data.lat}, Lng: ${response.data.lng}")
                }
                response.data
            } catch (e: Exception) {
                Log.e("MapScreen", "Error fetching mountain data", e)
                null
            }
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(LatLng(35.8447943443487, 127.11199020254), 14.0)
    }

    LaunchedEffect(key1 = mountainData) {
        mountainData?.let {
            cameraPositionState.position = CameraPosition(LatLng(it.lng, it.lat), 14.0)
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
                        state = rememberMarkerState(position = LatLng(it.lng, it.lat)),
                        captionText = it.mountainName,
                        captionTextSize = 14.sp,
                        captionMinZoom = 12.0
                    )
                }
            }
        }
        Box(modifier = Modifier.weight(2f)) {
            InfoPanel(mountainData?.height ?: 0, stepCount, distanceMoved)
        }
    }
}


@Composable
fun InfoPanel(elevation: Int, steps: Int, distance: Float) {
    Row(modifier = Modifier.fillMaxWidth()) {
        InfoItem(title = "고도", value = "$elevation m")
        InfoItem(title = "걸음 수", value = "$steps 걸음")
        InfoItem(title = "움직인 거리", value = String.format("%.2f km", distance))
    }
}

@Composable
fun InfoItem(title: String, value: String) {
    Column(modifier = Modifier
        .weight(1f)
        .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}

