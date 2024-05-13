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
import androidx.compose.ui.graphics.Color
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
import com.naver.maps.map.compose.PathOverlay
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

//@ExperimentalNaverMapApi
//@Composable
//fun MapScreen() {
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition(LatLng(35.21523156865972, 126.33359543068424), 15.0)
//    }
//    val pathOverlay = remember { PathOverlay() }
//
//    // MutableState를 사용하여 동적으로 데이터를 관리
//    val pathData = remember { mutableStateOf<List<LatLng>>(listOf()) }
//
//    // API 요청 및 데이터 로드
//    LaunchedEffect(true) {
//        try {
//            val response = RetrofitInstance.api.getAllCourses()
//            if (response.status == 200 && response.data.course.isNotEmpty()) {
//                // API 응답 데이터를 LatLng 리스트로 변환
//                pathData.value = response.data.course.first().locationDataList.map {
//                    LatLng(it.lat, it.lng)
//                }
//                // 로그로 확인
//                Log.d("MapScreen", "Received locations: ${pathData.value}")
//            } else {
//                Log.d("MapScreen", "Failed to load courses or empty list: Status ${response.status}")
//            }
//        } catch (e: Exception) {
//            Log.e("MapScreen", "Error fetching courses", e)
//        }
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        NaverMap(
//            modifier = Modifier.fillMaxSize(),
//            cameraPositionState = cameraPositionState,
//            properties = MapProperties(
//                locationTrackingMode = LocationTrackingMode.Follow,
//                mapType = MapType.Terrain,
//                isMountainLayerGroupEnabled = true
//            )
//        ) {
//            PathOverlay(
//                coords = COORDS_2,
//                width = 5.dp,
//                color = Color.Red
//            )
//            Log.d("경로", "${COORDS_2}")
////            // 지도에 경로를 그리는 PathOverlay 설정, 동적으로 업데이트된 데이터 사용
////            if (pathData.value.isNotEmpty()) {
////                PathOverlay().apply {
////                    coords = pathData.value
////                    Log.d("경로","${coords}")
////                    width = with(LocalDensity.current) {10.dp.toPx().toInt() }
////                    Log.d("넓이","${width}")
////                    color = android.graphics.Color.rgb(235, 152, 52)
////                    Log.d("색","${color}")
////                    map = map
////                    Log.d("맵","${map}")
////                }
////            } else {
////                pathOverlay.map = null // 경로 데이터가 없으면 맵에서 오버레이 제거
////            }
//        }
//    }
//}
//private val COORDS_2 = listOf(
//    LatLng(35.21523156865972, 126.33359543068424),
//    LatLng(35.21511628487386, 126.33370499990868),
//    LatLng(35.21508157342498, 126.33378910376335),
//    LatLng(35.215074738366496, 126.33391912406718),
//    LatLng(35.21505779063016, 126.33396656014465),
//    LatLng(35.21503806969774, 126.33397350504232),
//    LatLng(35.21500797899812, 126.33399010208622),
//    LatLng(35.21493036347349, 126.3340039310333)
//)

@ExperimentalNaverMapApi
@Composable
fun MapScreen() {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(LatLng(35.21523156865972, 126.33359543068424), 15.0)
    }
    val pathData = remember { mutableStateOf<List<LatLng>>(listOf()) }
    val isDataLoaded = remember { mutableStateOf(false) }  // 데이터 로드 완료 상태

    LaunchedEffect(true) {
        try {
            val response = RetrofitInstance.api.getAllCourses()
            if (response.status == 200 && response.data.course.isNotEmpty()) {
                pathData.value = response.data.course.first().locationDataList.map {
                    LatLng(it.lat, it.lng)
                }
                isDataLoaded.value = true  // 데이터 로드 완료 플래그 설정
            }
        } catch (e: Exception) {
            Log.e("MapScreen", "Error fetching courses", e)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isDataLoaded.value && pathData.value.size >= 2) {  // 데이터가 로드되고, 충분한 좌표가 있는지 확인
            NaverMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    locationTrackingMode = LocationTrackingMode.Follow,
                    mapType = MapType.Terrain,
                    isMountainLayerGroupEnabled = true
                )
            ) {
                PathOverlay(
                    coords = pathData.value,
                    width = 5.dp,
                    color = Color.Red
                )
            }
        }
    }
}
