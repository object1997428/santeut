package com.example.testcourse

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.testcourse.ui.theme.TestcourseTheme
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapType
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.util.FusedLocationSource
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.Point
import org.locationtech.jts.operation.distance.DistanceOp
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

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
) {
    val validLocationDataList: List<LocationData>
        get() = locationDataList.filter { !it.lat.isNaN() && !it.lng.isNaN() }
}

data class LocationData(
    val lat: Double,
    val lng: Double
)

data class MountainDetailResponse(
    val status: Int,
    val data: MountainDetailData
)

data class MountainDetailData(
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

interface MountainApiService {
    @GET("mountain/v2/442/all-course")
    suspend fun getAllCourses(): PathResponse

    @GET("mountain/v2/442")
    suspend fun getMountainDetail(): MountainDetailResponse
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
    private lateinit var locationSource: FusedLocationSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        setContent {
            TestcourseTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MapScreen(this, locationSource)
                }
            }
        }

        // 위치 권한 요청
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}

@ExperimentalNaverMapApi
@SuppressLint("MissingPermission")
@Composable
fun MapScreen(context: Context, locationSource: FusedLocationSource) {
    val cameraPositionState = rememberCameraPositionState()
    val pathData = remember { mutableStateOf<List<CourseDetail>>(listOf()) }
    val mountainDetailData = remember { mutableStateOf<MountainDetailData?>(null) }
    val currentLocation = remember { mutableStateOf<LatLng?>(null) }
    val isCameraInitialized = remember { mutableStateOf(false) } // 카메라 초기화 플래그
    val showAlert = remember { mutableStateOf(false) } // 경고 표시 플래그

    LaunchedEffect(true) {
        try {
            val mountainResponse = RetrofitInstance.api.getMountainDetail()
            if (mountainResponse.status == 200) {
                mountainDetailData.value = mountainResponse.data
                Log.d("MapScreen", "Received mountain detail: ${mountainResponse.data}")
            } else {
                Log.d("MapScreen", "Failed to load mountain detail: Status ${mountainResponse.status}")
            }

            val pathResponse = RetrofitInstance.api.getAllCourses()
            if (pathResponse.status == 200 && pathResponse.data.course.isNotEmpty()) {
                pathData.value = pathResponse.data.course
                Log.d("MapScreen", "Received courses: ${pathResponse.data.course}")
            } else {
                Log.d("MapScreen", "Failed to load courses or empty list: Status ${pathResponse.status}")
            }
        } catch (e: Exception) {
            Log.e("MapScreen", "Error fetching data", e)
        }
    }

    // 위치 권한 확인
    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // 권한 요청 로직 필요
        return
    }

    // 위치 요청 설정
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
        .setMinUpdateIntervalMillis(5000L)
        .build()

    // 위치 업데이트 콜백
    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                val latLng = LatLng(location.latitude, location.longitude)
                currentLocation.value = latLng
                Log.d("현재위치", "Current location: ${location.latitude}, ${location.longitude}")

                // 카메라 위치를 현재 위치로 업데이트 (초기에 한 번만)
                if (!isCameraInitialized.value) {
                    cameraPositionState.position = CameraPosition(latLng, 15.0)
                    isCameraInitialized.value = true
                }

                // 경로 데이터가 있을 경우에만 경로 이탈 여부 확인
                if (pathData.value.isNotEmpty()) {
                    checkRouteDeviation(location.latitude, location.longitude, pathData.value) { isDeviated ->
                        showAlert.value = isDeviated
                    }
                }
            }
        }
    }

    // 위치 업데이트 시작
    LaunchedEffect(true) {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NaverMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            locationSource = locationSource, // 위치 소스 설정
            properties = MapProperties(
                locationTrackingMode = LocationTrackingMode.Follow,
                mapType = MapType.Terrain,
                isMountainLayerGroupEnabled = true
            )
        ) {
            pathData.value.forEach { courseDetail -> // 모든 경로를 순회하며 그리기
                val path = courseDetail.validLocationDataList.map { LatLng(it.lat, it.lng) }
                if (path.size >= 2) { // 경로 데이터가 두 개 이상인 경우에만 PathOverlay를 그림
                    PathOverlay(
                        coords = path,
                        width = 3.dp,
                        color = Color.Green,
                        outlineWidth = 1.dp,
                        outlineColor = Color.Red,
                        tag = courseDetail.courseId
                    )
                    Log.d("경로", "${path}")
                }
            }
        }


//            pathData.value.firstOrNull()?.let { courseDetail ->
//                val path = courseDetail.validLocationDataList.map { LatLng(it.lat, it.lng) }
//                if (path.size >= 2) { // 경로 데이터가 두 개 이상인 경우에만 PathOverlay를 그림
//                    PathOverlay(
//                        coords = path,
//                        width = 3.dp,
//                        color = Color.Green,
//                        outlineWidth = 1.dp,
//                        outlineColor = Color.Red,
//                        tag = courseDetail.courseId
//                    )
//                    Log.d("경로","${path}")
//                }
//            }
//        }


        if (showAlert.value) {
            AlertDialog(
                onDismissRequest = { showAlert.value = false },
                title = { Text(text = "경고") },
                text = { Text(text = "경로를 이탈했습니다!") },
                confirmButton = {
                    Button(onClick = { showAlert.value = false }) {
                        Text("확인")
                    }
                }
            )
        }
    }
}

fun checkRouteDeviation(latitude: Double, longitude: Double, pathData: List<CourseDetail>, onDeviation: (Boolean) -> Unit) {
    val geometryFactory = GeometryFactory()
    val userLocation: Point = geometryFactory.createPoint(Coordinate(longitude, latitude))

    Log.d("현재 유저 위치", "Current user location: Latitude: $latitude, Longitude: $longitude")
    Log.d("유저 위치(포인트)", "User location (Point): $userLocation")

//    pathData.forEach { courseDetail ->
    pathData.firstOrNull()?.let { courseDetail -> // 첫 번째 경로에 대해서만 처리
        // 유효한 위치 데이터 리스트를 사용하여 경로 생성
        val coordinates = courseDetail.validLocationDataList.map { Coordinate(it.lng, it.lat) }.toTypedArray()

        coordinates.forEach {
            Log.d("생성된 Coordinate", "Coordinate: Latitude: ${it.y}, Longitude: ${it.x}")
        }
        Log.d("유저 위치(포인트)", "User location (Point): $userLocation")
        val predefinedRoute: LineString = geometryFactory.createLineString(coordinates)
        Log.d("Predefined route (LineString)", "Predefined route: ${predefinedRoute.coordinates.joinToString()}")

        val distanceOp = DistanceOp(predefinedRoute, userLocation)
        val minDistance = distanceOp.distance()
        val distanceInMeters = minDistance * 111319.9 // 1 degree ≈ 111.32 km

        Log.d("거리와의 차이", "Distance to route: $distanceInMeters meters")

        val isDeviated = distanceInMeters > 20.0
        if (isDeviated) {
            Log.d("경고", "User has deviated from the path: ${courseDetail.courseId}")
            onDeviation(true)
        } else {
            Log.d("checkRouteDeviation", "User is on the path: ${courseDetail.courseId}")
            onDeviation(false)
        }
    }
}
