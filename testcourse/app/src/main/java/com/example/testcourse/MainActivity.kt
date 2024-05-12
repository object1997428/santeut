package com.example.testcourse

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.testcourse.ui.theme.TestcourseTheme
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapType
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.overlay.PathOverlay
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor


data class PathResponse(
    val status: Int,
    val data: CourseData
)

data class CourseData(
    val course: List<CourseDetail>
)

data class CourseDetail(
    val courseId: Long,
    val distance: Double,
    val locationDataList: List<LocationData>
)

data class LocationData(
    val lat: Double,
    val lng: Double
)

interface MountainApiService {
    @GET("mountain/v2/7/all-course")
    suspend fun getAllCourses(): PathResponse
}

class AuthInterceptor(val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .method(original.method, original.body)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}

object RetrofitInstance {
    private const val BASE_URL = "https://k10e201.p.ssafy.io/api/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MDAxIiwiaWF0IjoxNzE0NzA2MTEwLCJleHAiOjE3NDYyMzYxMTB9.c0bRrGh0NGA8HPd_oCMCUPfmQaAwzswftvHdv8xulzg"))  // 인증 토큰 추가
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }) // 로깅 인터셉터 추가
        .build()

    val api: MountainApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MountainApiService::class.java)
    }
}

@ExperimentalNaverMapApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestcourseTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MapScreen()
                }
            }
        }
    }
}

@ExperimentalNaverMapApi
@Composable
fun MapScreen() {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(LatLng(35.21348272428957, 126.3356490483132), 15.0)
    }

    val pathData = remember { mutableStateOf<List<LatLng>>(listOf()) }

    // API 요청 및 데이터 로드
    LaunchedEffect(true) {
        try {
            val response = RetrofitInstance.api.getAllCourses()
            if (response.status == 200 && response.data.course.isNotEmpty()) {
                val locations = response.data.course.first().locationDataList.map {
                    LatLng(it.lat, it.lng)
                }
                if (locations.size >= 2) {
                    pathData.value = locations
                } else {
                    // 적절한 오류 처리 또는 사용자 알림
                    Log.d("MapScreen", "Insufficient locations received: ${locations.size}")
                    return@LaunchedEffect  // 중요: 조건이 충족되지 않으면 더 이상 진행하지 않습니다.
                }
                // 로그 출력
                Log.d("MapScreen", "Received locations: $locations")
            } else {
                Log.d(
                    "MapScreen",
                    "Failed to load courses or empty list: Status ${response.status}"
                )
            }
        } catch (e: Exception) {
            Log.e("MapScreen", "Error fetching courses", e)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NaverMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                locationTrackingMode = LocationTrackingMode.Follow,
                mapType = MapType.Terrain,
                isMountainLayerGroupEnabled = true
            )
        ) {
            // PathOverlay 설정을 조건부로 실행
            if (pathData.value.size >= 2) {
                PathOverlay().apply {
                    coords = pathData.value
                    width = with(LocalDensity.current) { 5.dp.toPx().toInt() }
                    color = MaterialTheme.colorScheme.onSurface.toArgb()
                    map = map
                }
            }
        }
    }
}

