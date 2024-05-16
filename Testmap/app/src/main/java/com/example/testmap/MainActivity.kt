////package com.example.testmap
////
////import android.Manifest
////import android.content.Context
////import android.content.pm.PackageManager
////import android.graphics.*
////import android.os.Bundle
////import android.util.Log
////import android.hardware.Sensor
////import android.hardware.SensorEvent
////import android.hardware.SensorEventListener
////import android.hardware.SensorManager
////import androidx.activity.ComponentActivity
////import androidx.activity.compose.setContent
////import androidx.compose.foundation.layout.*
////import androidx.compose.material3.*
////import androidx.compose.runtime.*
////import androidx.compose.ui.Alignment
////import androidx.compose.ui.Modifier
////import androidx.compose.ui.platform.LocalContext
////import androidx.compose.ui.unit.dp
////import androidx.compose.ui.unit.sp
////import androidx.core.app.ActivityCompat
////import androidx.core.content.ContextCompat.getSystemService
////import com.example.testmap.ui.theme.TestmapTheme
////import com.google.android.gms.location.LocationServices
////import com.google.android.gms.location.FusedLocationProviderClient
////import com.naver.maps.geometry.LatLng
////import com.naver.maps.map.CameraPosition
////import com.naver.maps.map.compose.*
////import kotlinx.coroutines.*
////import kotlinx.coroutines.tasks.await
////import okhttp3.*
////import retrofit2.Retrofit
////import retrofit2.converter.gson.GsonConverterFactory
////import retrofit2.http.GET
////import com.google.gson.Gson
////import com.naver.maps.map.overlay.OverlayImage
////import java.io.ByteArrayOutputStream
////
////// API 인터페이스 정의
////interface MountainService {
////    @GET("mountain/v2/2")
////    suspend fun getMountainData(): MountainResponse
////}
////
////// 데이터 클래스 정의
////data class MountainResponse(
////    val status: Int,
////    val data: MountainData
////)
////
////data class MountainData(
////    val mountainName: String,
////    val address: String,
////    val description: String,
////    val height: Int,
////    val courseCount: Int,
////    val lat: Double,
////    val lng: Double,
////    val views: Int,
////    val image: String?
////)
////
////// 인증 토큰을 추가하는 Interceptor 구현
////class AuthInterceptor(private val token: String) : Interceptor {
////    override fun intercept(chain: Interceptor.Chain): Response {
////        val originalRequest = chain.request()
////        val newRequest = originalRequest.newBuilder()
////            .addHeader("Authorization", "Bearer $token")
////            .build()
////        return chain.proceed(newRequest)
////    }
////}
////
////// OkHttpClient 및 Retrofit 서비스 설정
////fun createMountainService(token: String): MountainService {
////    val client = OkHttpClient.Builder()
////        .addInterceptor(AuthInterceptor(token))
////        .build()
////
////    return Retrofit.Builder()
////        .baseUrl("https://k10e201.p.ssafy.io/api/")
////        .client(client)
////        .addConverterFactory(GsonConverterFactory.create())
////        .build()
////        .create(MountainService::class.java)
////}
////
////@ExperimentalNaverMapApi
////class MainActivity : ComponentActivity() {
////    private var webSocket: WebSocket? = null
////    private val webSocketUrl = "wss://k10e201.p.ssafy.io/api/hiking/chat/rooms/1"
////
////    companion object {
////        private const val REQUEST_CODE_ACTIVITY_RECOGNITION = 1
////    }
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        setContent {
////            TestmapTheme {
////                Surface(
////                    modifier = Modifier.fillMaxSize(),
////                    color = MaterialTheme.colorScheme.background
////                ) {
////                    MapScreen(
////                        context = this@MainActivity,
////                        token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNyIsInVzZXJOaWNrbmFtZSI6IlRlc3RTU0FGWTU1NSIsImlhdCI6MTcxNTY2OTM3NiwiZXhwIjoxNzQ3MTk5Mzc2fQ.-9kQCi15rJaeKlVtFk40cxwjKXCvqJdGvp1GCLJ5ny8",
////                        ::startWebSocket,
////                        ::stopWebSocket,
////                    )
////                }
////            }
////        }
////    }
////
////    fun startWebSocket(updateLocation: (String, Double, Double, String?) -> Unit, context: Context, token: String) {
////        val request = Request.Builder()
////            .url(webSocketUrl)
////            .addHeader("Authorization", "Bearer $token") // 헤더에 토큰 추가
////            .build()
////
////        var locationSendingJob: Job? = null  // 위치 전송 코루틴 작업을 참조하기 위한 변수
////
////        val listener = object : WebSocketListener() {
////            override fun onMessage(webSocket: WebSocket, text: String) {
////                Log.d("WebSocket", "Received message: $text")
////                try {
////                    val message = Gson().fromJson(text, WebSocketMessage::class.java)
////                    updateLocation(message.userNickname, message.lat.toDouble(), message.lng.toDouble(), message.userProfile)
////                } catch (e: Exception) {
////                    Log.e("WebSocket", "Error parsing message: ${e.localizedMessage}", e)
////                }
////            }
////
////            override fun onOpen(webSocket: WebSocket, response: Response) {
////                // 웹소켓 연결이 성공하면 초기 메시지를 보내고 위치 정보를 주기적으로 보내기 시작
////                webSocket.send("{\"type\": \"enter\"}")
////                Log.d("WebSocket", "WebSocket opened and initial message sent.")
////
////                locationSendingJob = CoroutineScope(Dispatchers.IO).launch {
////                    while (isActive) {
////                        delay(2000)  // 2초마다 반복
////                        val currentLocation = getCurrentLocation(context)
////                        currentLocation?.let {
////                            val locationJson = Gson().toJson(WebSocketSendMessage(
////                                type = "message",
////                                lat = it.latitude.toString(),
////                                lng = it.longitude.toString()
////                            ))
////                            webSocket.send(locationJson)
////                        }
////                    }
////                }
////            }
////
////            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
////                Log.d("WebSocket", "WebSocket is closing: $reason")
////                locationSendingJob?.cancel()  // 위치 전송 코루틴 작업 취소
////            }
////
////            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
////                Log.e("WebSocket", "WebSocket connection failure: ${t.localizedMessage}", t)
////                locationSendingJob?.cancel()  // 실패 시 위치 전송 코루틴 작업 취소
////            }
////        }
////
////        webSocket = OkHttpClient().newWebSocket(request, listener)
////    }
////
////    fun stopWebSocket() {
////        webSocket?.send("{\"type\": \"quit\"}")
////        webSocket?.close(1000, "Activity Ended")
////        webSocket = null
////    }
////}
////
////@ExperimentalNaverMapApi
////@Composable
////fun MapScreen(
////    context: Context,
////    token: String,
////    startWebSocket: (updateLocation: (String, Double, Double, String?) -> Unit, Context, String) -> Unit,
////    stopWebSocket: () -> Unit,
////) {
////    val mountainService = remember { createMountainService(token) }
////    var mountainData by remember { mutableStateOf<MountainData?>(null) }
////    var userPositions by remember { mutableStateOf<Map<String, LatLng>>(mapOf()) }
////
////    var stepCount by remember { mutableStateOf(0) }
////    var distanceMoved by remember { mutableStateOf(0f) }
////    var timerRunning by remember { mutableStateOf(false) }
////    var elapsedTime by remember { mutableStateOf(0) }
////    var altitude by remember { mutableStateOf(0f) }  // 고도 상태를 저장하는 변수
////
////    // 사용자 위치 마커 상태를 관리하기 위해 MutableMap을 사용
////    val userMarkerStates by remember { mutableStateOf<MutableMap<String, MarkerState>>(mutableMapOf()) }
////    val userIcons by remember { mutableStateOf<MutableMap<String, OverlayImage?>>(mutableMapOf()) }
////
////    // userPositions가 변경될 때마다 마커 상태를 업데이트
////    LaunchedEffect(userPositions) {
////        userPositions.forEach { (nickname, position) ->
////            userMarkerStates[nickname] = MarkerState(position = position)
////        }
////    }
////
////    val sensorManager = getSystemService(context, SensorManager::class.java)
////    val stepDetectorSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
////
////    DisposableEffect(Unit) {
////        val sensorEventListener = object : SensorEventListener {
////            override fun onSensorChanged(event: SensorEvent) {
////                if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
////                    stepCount += event.values[0].toInt()
////                }
////            }
////            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
////        }
////
////        stepDetectorSensor?.let {
////            sensorManager?.registerListener(sensorEventListener, it, SensorManager.SENSOR_DELAY_UI)
////        }
////
////        onDispose {
////            sensorManager?.unregisterListener(sensorEventListener)
////        }
////    }
////
////    // 기압 센서 리스너 설정 및 고도 계산
////    DisposableEffect(Unit) {
////        val pressureSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_PRESSURE)
////        val sensorEventListener = object : SensorEventListener {
////            override fun onSensorChanged(event: SensorEvent) {
////                if (event.sensor.type == Sensor.TYPE_PRESSURE) {
////                    val pressure = event.values[0]  // 기압값 읽기
////                    altitude = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressure)
////                }
////            }
////            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
////        }
////
////        pressureSensor?.let {
////            sensorManager?.registerListener(sensorEventListener, it, SensorManager.SENSOR_DELAY_UI)
////        }
////
////        onDispose {
////            sensorManager?.unregisterListener(sensorEventListener)
////        }
////    }
////
////    LaunchedEffect(timerRunning) {
////        if (timerRunning) {
////            startWebSocket({ nickname, lat, lng, imageUrl ->
////                userPositions = userPositions.toMutableMap().apply {
////                    this[nickname] = LatLng(lat, lng)
////                }
////                if (imageUrl != null) {
////                    CoroutineScope(Dispatchers.IO).launch {
////                        val icon = getOverlayImageFromUrl(imageUrl, 100, 100)
////                        withContext(Dispatchers.Main) {
////                            userIcons[nickname] = icon
////                        }
////                    }
////                }
////            }, context, token) // 토큰 전달
////            while (timerRunning) {
////                delay(1000)
////                elapsedTime += 1
////            }
////        } else {
////            stopWebSocket()
////            elapsedTime = 0
////            userMarkerStates.clear() // 사용자 마커 상태 초기화
////            userPositions = mapOf()
////        }
////    }
////
////    val cameraPositionState = rememberCameraPositionState {
////        position = CameraPosition(LatLng(35.8447943443487, 127.11199020254), 16.0)
////    }
////
////    val uiSettings = remember {
////        MapUiSettings(
////            isZoomControlEnabled = true,
////            isLocationButtonEnabled = true,
////            isCompassEnabled = true
////        )
////    }
////
////    Column(modifier = Modifier.fillMaxSize()) {
////        Box(modifier = Modifier.weight(8f)) {
////            NaverMap(
////                modifier = Modifier.fillMaxSize(),
////                cameraPositionState = cameraPositionState,
////                locationSource = rememberFusedLocationSource(isCompassEnabled = uiSettings.isCompassEnabled),
////                properties = MapProperties(
////                    locationTrackingMode = LocationTrackingMode.Follow,
////                    mapType = MapType.Terrain,
////                    isMountainLayerGroupEnabled = true
////                ),
////                uiSettings = uiSettings
////            ) {
////                val context = LocalContext.current
////                val resizedIcon = remember { resizeMarkerIcon(context, R.drawable.custom_marker, 100, 100) }
////                mountainData?.let {
////                    Marker(
////                        state = rememberMarkerState(position = LatLng(it.lat, it.lng)),
////                        captionText = it.mountainName,
////                        captionTextSize = 14.sp,
////                        captionMinZoom = 12.0,
////                        icon = resizedIcon  // 마커 이미지 설정
////                    )
////                }
////                // 사용자 마커 렌더링
////                userMarkerStates.forEach { (nickname, markerState) ->
////                    val icon = userIcons[nickname] ?: resizedIcon
////                    Marker(
////                        state = markerState,
////                        captionText = nickname,
////                        captionTextSize = 14.sp,
////                        captionMinZoom = 12.0,
////                        icon = icon  // 마커 이미지 설정
////                    )
////                }
////            }
////            Button(
////                onClick = {
////                    timerRunning = !timerRunning
////                },
////                modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
////            ) {
////                Text(if (timerRunning) "중지" else "등산 시작")
////            }
////        }
////        Box(modifier = Modifier.weight(2f)) {
////            InfoPanel(mountainData?.height ?: 0, stepCount, distanceMoved, elapsedTime, altitude.toInt())
////        }
////    }
////}
////
////suspend fun downloadImage(url: String): Bitmap? {
////    return try {
////        val result = withContext(Dispatchers.IO) {
////            val client = OkHttpClient()
////            val request = Request.Builder().url(url).build()
////            val response = client.newCall(request).execute()
////            response.body?.byteStream()?.use {
////                BitmapFactory.decodeStream(it)
////            }
////        }
////        result
////    } catch (e: Exception) {
////        Log.e("ImageDownloadError", "Error downloading image", e)
////        null
////    }
////}
////
////fun getCircularBitmap(bitmap: Bitmap): Bitmap {
////    val size = Math.min(bitmap.width, bitmap.height)
////    val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
////
////    val canvas = Canvas(output)
////    val paint = Paint()
////    val rect = Rect(0, 0, size, size)
////    val rectF = RectF(rect)
////
////    paint.isAntiAlias = true
////    canvas.drawARGB(0, 0, 0, 0)
////    canvas.drawOval(rectF, paint)
////
////    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
////    canvas.drawBitmap(bitmap, rect, rect, paint)
////
////    return output
////}
////
////suspend fun getOverlayImageFromUrl(url: String, width: Int, height: Int): OverlayImage? {
////    val bitmap = downloadImage(url)
////    return bitmap?.let {
////        val circularBitmap = getCircularBitmap(it)
////        val resizedBitmap = Bitmap.createScaledBitmap(circularBitmap, width, height, false)
////        val stream = ByteArrayOutputStream()
////        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
////        val byteArray = stream.toByteArray()
////        val bitmapResized = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
////        OverlayImage.fromBitmap(bitmapResized)
////    }
////}
////
////fun resizeMarkerIcon(context: Context, drawableResId: Int, width: Int, height: Int): OverlayImage {
////    val bitmap = BitmapFactory.decodeResource(context.resources, drawableResId)
////    val circularBitmap = getCircularBitmap(bitmap)
////    val resizedBitmap = Bitmap.createScaledBitmap(circularBitmap, width, height, false)
////
////    // Bitmap을 ByteArray로 변환하여 OverlayImage.fromBitmap()에 전달
////    val stream = ByteArrayOutputStream()
////    resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
////    val byteArray = stream.toByteArray()
////    val bitmapResized = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
////
////    return OverlayImage.fromBitmap(bitmapResized)
////}
////
////@Composable
////fun InfoPanel(elevation: Int, steps: Int, distance: Float, timeInSeconds: Int, altitude: Int) {
////    val hours = timeInSeconds / 3600
////    val minutes = (timeInSeconds % 3600) / 60
////    val seconds = timeInSeconds % 60
////
////    Surface(
////        modifier = Modifier.fillMaxWidth().padding(16.dp),
////        color = MaterialTheme.colorScheme.surfaceVariant,
////        shape = MaterialTheme.shapes.medium,
////        shadowElevation = 4.dp
////    ) {
////        Row(
////            modifier = Modifier.fillMaxWidth().padding(8.dp),
////            horizontalArrangement = Arrangement.SpaceEvenly
////        ) {
////            InfoItem(title = "고도", value = "$altitude m")
////            InfoItem(title = "걸음 수", value = "$steps 걸음")
////            InfoItem(title = "움직인 거리", value = String.format("%.2f km", distance))
////            InfoItem(title = "경과 시간", value = String.format("%02d시 %02d분 %02d초", hours, minutes, seconds))
////        }
////    }
////}
////
////@Composable
////fun InfoItem(title: String, value: String) {
////    Column(
////        modifier = Modifier.padding(8.dp),
////        horizontalAlignment = Alignment.CenterHorizontally
////    ) {
////        Text(text = title, style = MaterialTheme.typography.titleMedium)
////        Text(text = value, style = MaterialTheme.typography.bodyLarge)
////    }
////}
////
////data class WebSocketMessage(
////    val type: String,
////    val partyId: Int,
////    val userId: Int,
////    val userNickname: String,
////    val userProfile: String?, // 이미지 URL 필드 추가
////    val lat: String,
////    val lng: String
////
////)
////
////data class WebSocketSendMessage(
////    val type: String,
////    val lat: String,
////    val lng: String
////)
////data class UserLocationData(
////    val latitude: Double,
////    val longitude: Double
////)
////
////// 위치 정보를 비동기적으로 가져오는 함수
////suspend fun getCurrentLocation(context: Context): UserLocationData? {
////    val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
////
////    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
////        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////        // 권한 요청 로직 필요
////        return null
////    }
////
////    return try {
////        val location = fusedLocationProviderClient.lastLocation.await()
////        if (location != null) {
////            UserLocationData(location.latitude, location.longitude)
////        } else {
////            null
////        }
////    } catch (e: Exception) {
////        Log.e("LocationError", "Error getting location", e)
////        null
////    }
////}
//
//
//package com.example.testmap
//
//import android.Manifest
//import android.content.ContentValues.TAG
//import android.content.Context
//import android.content.pm.PackageManager
//import android.graphics.*
//import android.os.Bundle
//import android.util.Log
//import android.hardware.Sensor
//import android.hardware.SensorEvent
//import android.hardware.SensorEventListener
//import android.hardware.SensorManager
//import android.os.Build
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.graphics.Color
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.core.content.ContextCompat.getSystemService
//import com.example.testmap.ui.theme.TestmapTheme
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.tasks.OnCompleteListener
//import com.google.firebase.FirebaseApp
//import com.google.firebase.messaging.FirebaseMessaging
//import com.naver.maps.geometry.LatLng
//import com.naver.maps.map.CameraPosition
//import com.naver.maps.map.compose.*
//import kotlinx.coroutines.*
//import kotlinx.coroutines.tasks.await
//import okhttp3.*
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.GET
//import com.google.gson.Gson
//import com.naver.maps.map.overlay.OverlayImage
//import java.io.ByteArrayOutputStream
//import org.locationtech.jts.geom.Coordinate
//import org.locationtech.jts.geom.GeometryFactory
//import org.locationtech.jts.geom.LineString
//import org.locationtech.jts.geom.Point
//import org.locationtech.jts.operation.distance.DistanceOp
//
//// API 인터페이스 정의
//interface MountainService {
//    @GET("mountain/v2/2")
//    suspend fun getMountainData(): MountainResponse
//
//    @GET("mountain/v2/442/all-course")
//    suspend fun getAllCourses(): PathResponse
//}
//
//// 데이터 클래스 정의
//data class MountainResponse(
//    val status: Int,
//    val data: MountainData
//)
//
//data class MountainData(
//    val mountainName: String,
//    val address: String,
//    val description: String,
//    val height: Int,
//    val courseCount: Int,
//    val lat: Double,
//    val lng: Double,
//    val views: Int,
//    val image: String?
//)
//
//data class PathResponse(
//    val status: Int,
//    val data: CourseData
//)
//
//data class CourseData(
//    val course: List<CourseDetail>
//)
//
//data class CourseDetail(
//    val courseId: Long,
//    val distance: Double,
//    val locationDataList: List<LocationData>
//) {
//    val validLocationDataList: List<LocationData>
//        get() = locationDataList.filter { !it.lat.isNaN() && !it.lng.isNaN() }
//}
//
//data class LocationData(
//    val lat: Double,
//    val lng: Double
//)
//
//// 인증 토큰을 추가하는 Interceptor 구현
//class AuthInterceptor(private val token: String) : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val originalRequest = chain.request()
//        val newRequest = originalRequest.newBuilder()
//            .addHeader("Authorization", "Bearer $token")
//            .build()
//        return chain.proceed(newRequest)
//    }
//}
//
//// OkHttpClient 및 Retrofit 서비스 설정
//fun createMountainService(token: String): MountainService {
//    val client = OkHttpClient.Builder()
//        .addInterceptor(AuthInterceptor(token))
//        .build()
//
//    return Retrofit.Builder()
//        .baseUrl("https://k10e201.p.ssafy.io/api/")
//        .client(client)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//        .create(MountainService::class.java)
//}
//
//@ExperimentalNaverMapApi
//class MainActivity : ComponentActivity() {
//    private var webSocket: WebSocket? = null
//    private val webSocketUrl = "wss://k10e201.p.ssafy.io/api/hiking/chat/rooms/1"
//
//    companion object {
//        private const val REQUEST_CODE_ACTIVITY_RECOGNITION = 1
//        private const val TAG = "MainActivity"
//    }
//
//    // 권한 요청을 위한 런처 선언
//    private val requestPermissionLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestPermission(),
//    ) { isGranted: Boolean ->
//        if (isGranted) {
//            // FCM SDK (and your app) can post notifications.
//        } else {
//            // TODO: Inform user that that your app will not show notifications.
//        }
//    }
//
//    // 알림 권한 요청 함수
//    private fun askNotificationPermission() {
//        // This is only necessary for API level >= 33 (TIRAMISU)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
//                PackageManager.PERMISSION_GRANTED
//            ) {
//                // FCM SDK (and your app) can post notifications.
//            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
//                // TODO: display an educational UI explaining to the user the features that will be enabled
//                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
//                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
//                //       If the user selects "No thanks," allow the user to continue without notifications.
//            } else {
//                // Directly ask for the permission
//                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
//            }
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Firebase 초기화
//        FirebaseApp.initializeApp(this)
//
//        // 알림 권한 요청
//        askNotificationPermission()
//
//        // Firebase 초기화 및 FCM 토큰 가져오기
//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
//                return@OnCompleteListener
//            }
//
//            // Get new FCM registration token
//            val token = task.result
//
//            // Log and toast
//            val msg = getString(R.string.msg_token_fmt, token)
//            Log.d(TAG, msg)
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//        })
//
//        setContent {
//            TestmapTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    MapScreen(
//                        context = this@MainActivity,
//                        token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNyIsInVzZXJOaWNrbmFtZSI6IlRlc3RTU0FGWTU1NSIsImlhdCI6MTcxNTY2OTM3NiwiZXhwIjoxNzQ3MTk5Mzc2fQ.-9kQCi15rJaeKlVtFk40cxwjKXCvqJdGvp1GCLJ5ny8",
//                        ::startWebSocket,
//                        ::stopWebSocket,
//                    )
//                }
//            }
//        }
//    }
//
//    fun startWebSocket(updateLocation: (String, Double, Double, String?) -> Unit, context: Context, token: String) {
//        val request = Request.Builder()
//            .url(webSocketUrl)
//            .addHeader("Authorization", "Bearer $token")
//            .build()
//
//        var locationSendingJob: Job? = null
//
//        val listener = object : WebSocketListener() {
//            override fun onMessage(webSocket: WebSocket, text: String) {
//                Log.d("WebSocket", "Received message: $text")
//                try {
//                    val message = Gson().fromJson(text, WebSocketMessage::class.java)
//                    updateLocation(message.userNickname, message.lat.toDouble(), message.lng.toDouble(), message.userProfile)
//                } catch (e: Exception) {
//                    Log.e("WebSocket", "Error parsing message: ${e.localizedMessage}", e)
//                }
//            }
//
//            override fun onOpen(webSocket: WebSocket, response: Response) {
//                webSocket.send("{\"type\": \"enter\"}")
//                Log.d("WebSocket", "WebSocket opened and initial message sent.")
//
//                locationSendingJob = CoroutineScope(Dispatchers.IO).launch {
//                    while (isActive) {
//                        delay(2000)
//                        val currentLocation = getCurrentLocation(context)
//                        currentLocation?.let {
//                            val locationJson = Gson().toJson(WebSocketSendMessage(
//                                type = "message",
//                                lat = it.latitude.toString(),
//                                lng = it.longitude.toString()
//                            ))
//                            webSocket.send(locationJson)
//                        }
//                    }
//                }
//            }
//
//            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
//                Log.d("WebSocket", "WebSocket is closing: $reason")
//                locationSendingJob?.cancel()
//            }
//
//            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
//                Log.e("WebSocket", "WebSocket connection failure: ${t.localizedMessage}", t)
//                locationSendingJob?.cancel()
//            }
//        }
//
//        webSocket = OkHttpClient().newWebSocket(request, listener)
//    }
//
//    fun stopWebSocket() {
//        webSocket?.send("{\"type\": \"quit\"}")
//        webSocket?.close(1000, "Activity Ended")
//        webSocket = null
//    }
//}
//
//@ExperimentalNaverMapApi
//@Composable
//fun MapScreen(
//    context: Context,
//    token: String,
//    startWebSocket: (updateLocation: (String, Double, Double, String?) -> Unit, Context, String) -> Unit,
//    stopWebSocket: () -> Unit,
//) {
//    val mountainService = remember { createMountainService(token) }
//    var mountainData by remember { mutableStateOf<MountainData?>(null) }
//    var pathData by remember { mutableStateOf<List<CourseDetail>>(listOf()) }
//    var userPositions by remember { mutableStateOf<Map<String, LatLng>>(mapOf()) }
//
//    var stepCount by remember { mutableStateOf(0) }
//    var distanceMoved by remember { mutableStateOf(0f) }
//    var timerRunning by remember { mutableStateOf(false) }
//    var elapsedTime by remember { mutableStateOf(0) }
//    var altitude by remember { mutableStateOf(0f) }
//
//    val userMarkerStates by remember { mutableStateOf<MutableMap<String, MarkerState>>(mutableMapOf()) }
//    val userIcons by remember { mutableStateOf<MutableMap<String, OverlayImage?>>(mutableMapOf()) }
//    val showAlert = remember { mutableStateOf(false) }
//
//    LaunchedEffect(userPositions) {
//        userPositions.forEach { (nickname, position) ->
//            userMarkerStates[nickname] = MarkerState(position = position)
//        }
//    }
//
//    LaunchedEffect(true) {
//        try {
//            val pathResponse = mountainService.getAllCourses()
//            if (pathResponse.status == 200 && pathResponse.data.course.isNotEmpty()) {
//                pathData = pathResponse.data.course
////                Log.d("MapScreen", "Received courses: ${pathResponse.data.course}")
//            } else {
//                Log.d("MapScreen", "Failed to load courses or empty list: Status ${pathResponse.status}")
//            }
//        } catch (e: Exception) {
//            Log.e("MapScreen", "Error fetching data", e)
//        }
//    }
//
//    val sensorManager = getSystemService(context, SensorManager::class.java)
//    val stepDetectorSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
//
//    DisposableEffect(Unit) {
//        val sensorEventListener = object : SensorEventListener {
//            override fun onSensorChanged(event: SensorEvent) {
//                if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
//                    stepCount += event.values[0].toInt()
//                }
//            }
//            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
//        }
//
//        stepDetectorSensor?.let {
//            sensorManager?.registerListener(sensorEventListener, it, SensorManager.SENSOR_DELAY_UI)
//        }
//
//        onDispose {
//            sensorManager?.unregisterListener(sensorEventListener)
//        }
//    }
//
//    DisposableEffect(Unit) {
//        val pressureSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_PRESSURE)
//        val sensorEventListener = object : SensorEventListener {
//            override fun onSensorChanged(event: SensorEvent) {
//                if (event.sensor.type == Sensor.TYPE_PRESSURE) {
//                    val pressure = event.values[0]
//                    altitude = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressure)
//                }
//            }
//            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
//        }
//
//        pressureSensor?.let {
//            sensorManager?.registerListener(sensorEventListener, it, SensorManager.SENSOR_DELAY_UI)
//        }
//
//        onDispose {
//            sensorManager?.unregisterListener(sensorEventListener)
//        }
//    }
//
//    LaunchedEffect(timerRunning) {
//        if (timerRunning) {
//            startWebSocket({ nickname, lat, lng, imageUrl ->
//                userPositions = userPositions.toMutableMap().apply {
//                    this[nickname] = LatLng(lat, lng)
//                }
//                if (!imageUrl.isNullOrEmpty()) {
//                    CoroutineScope(Dispatchers.IO).launch {
//                        val icon = getOverlayImageFromUrl(imageUrl, 100, 100)
//                        withContext(Dispatchers.Main) {
//                            userIcons[nickname] = icon
//                        }
//                    }
//                }
//            }, context, token)
//            while (timerRunning) {
//                delay(1000)
//                elapsedTime += 1
//                val currentLocation = getCurrentLocation(context)
//                currentLocation?.let {
//                    checkRouteDeviation(it.latitude, it.longitude, pathData) { isDeviated ->
//                        showAlert.value = isDeviated
//                    }
//                }
//            }
//        } else {
//            stopWebSocket()
//            elapsedTime = 0
//            userMarkerStates.clear()
//            userPositions = mapOf()
//        }
//    }
//
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition(LatLng(35.8447943443487, 127.11199020254), 16.0)
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
//        Box(modifier = Modifier.weight(8f)) {
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
//                val context = LocalContext.current
//                val resizedIcon = remember { resizeMarkerIcon(context, R.drawable.custom_marker, 100, 100) }
//                mountainData?.let {
//                    Marker(
//                        state = rememberMarkerState(position = LatLng(it.lat, it.lng)),
//                        captionText = it.mountainName,
//                        captionTextSize = 14.sp,
//                        captionMinZoom = 12.0,
//                        icon = resizedIcon
//                    )
//                }
//                pathData.forEach { courseDetail ->
//                    val path = courseDetail.validLocationDataList.map { LatLng(it.lat, it.lng) }
//                    if (path.size >= 2) {
//                        PathOverlay(
//                            coords = path,
//                            width = 3.dp,
//                            color = Color.Green,
//                            outlineWidth = 1.dp,
//                            outlineColor = Color.Red,
//                            tag = courseDetail.courseId
//                        )
//                    }
//                }
//                userMarkerStates.forEach { (nickname, markerState) ->
//                    val icon = userIcons[nickname] ?: resizedIcon
//                    Marker(
//                        state = markerState,
//                        captionText = nickname,
//                        captionTextSize = 14.sp,
//                        captionMinZoom = 12.0,
//                        icon = icon
//                    )
//                }
//            }
//            Button(
//                onClick = {
//                    timerRunning = !timerRunning
//                },
//                modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
//            ) {
//                Text(if (timerRunning) "중지" else "등산 시작")
//            }
//        }
//        Box(modifier = Modifier.weight(2f)) {
//            InfoPanel(altitude.toInt(), stepCount, distanceMoved, elapsedTime)
//        }
//        if (showAlert.value) {
//            AlertDialog(
//                onDismissRequest = { showAlert.value = false },
//                title = { Text(text = "경고") },
//                text = { Text(text = "경로를 이탈했습니다!") },
//                confirmButton = {
//                    Button(onClick = { showAlert.value = false }) {
//                        Text("확인")
//                    }
//                }
//            )
//        }
//    }
//}
//
//fun checkRouteDeviation(latitude: Double, longitude: Double, pathData: List<CourseDetail>, onDeviation: (Boolean) -> Unit) {
//    val geometryFactory = GeometryFactory()
//    val userLocation: Point = geometryFactory.createPoint(Coordinate(longitude, latitude))
//
//    pathData.firstOrNull()?.let { courseDetail ->
//        val coordinates = courseDetail.validLocationDataList.map { Coordinate(it.lng, it.lat) }.toTypedArray()
//        val predefinedRoute: LineString = geometryFactory.createLineString(coordinates)
//        val distanceOp = DistanceOp(predefinedRoute, userLocation)
//        val minDistance = distanceOp.distance()
//        val distanceInMeters = minDistance * 111319.9
//
//        val isDeviated = distanceInMeters > 20.0
//        onDeviation(isDeviated)
//    }
//}
//
//suspend fun downloadImage(url: String): Bitmap? {
//    return try {
//        val result = withContext(Dispatchers.IO) {
//            val client = OkHttpClient()
//            val request = Request.Builder().url(url).build()
//            val response = client.newCall(request).execute()
//            response.body?.byteStream()?.use {
//                BitmapFactory.decodeStream(it)
//            }
//        }
//        result
//    } catch (e: Exception) {
//        Log.e("ImageDownloadError", "Error downloading image", e)
//        null
//    }
//}
//
//fun getCircularBitmap(bitmap: Bitmap): Bitmap {
//    val size = Math.min(bitmap.width, bitmap.height)
//    val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
//
//    val canvas = Canvas(output)
//    val paint = Paint()
//    val rect = Rect(0, 0, size, size)
//    val rectF = RectF(rect)
//
//    paint.isAntiAlias = true
//    canvas.drawARGB(0, 0, 0, 0)
//    canvas.drawOval(rectF, paint)
//
//    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
//    canvas.drawBitmap(bitmap, rect, rect, paint)
//
//    return output
//}
//
//suspend fun getOverlayImageFromUrl(url: String, width: Int, height: Int): OverlayImage? {
//    if (url == null || url.isEmpty()) {
//        Log.e("ImageDownloadError", "Invalid URL: $url")
//        return null
//    }
//
//    val bitmap = downloadImage(url)
//    return bitmap?.let {
//        val circularBitmap = getCircularBitmap(it)
//        val resizedBitmap = Bitmap.createScaledBitmap(circularBitmap, width, height, false)
//        val stream = ByteArrayOutputStream()
//        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
//        val byteArray = stream.toByteArray()
//        val bitmapResized = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//        OverlayImage.fromBitmap(bitmapResized)
//    }
//}
//
//fun resizeMarkerIcon(context: Context, drawableResId: Int, width: Int, height: Int): OverlayImage {
//    val bitmap = BitmapFactory.decodeResource(context.resources, drawableResId)
//    val circularBitmap = getCircularBitmap(bitmap)
//    val resizedBitmap = Bitmap.createScaledBitmap(circularBitmap, width, height, false)
//
//    val stream = ByteArrayOutputStream()
//    resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
//    val byteArray = stream.toByteArray()
//    val bitmapResized = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//
//    return OverlayImage.fromBitmap(bitmapResized)
//}
//
//@Composable
//fun InfoPanel(altitude: Int, steps: Int, distance: Float, timeInSeconds: Int) {
//    val hours = timeInSeconds / 3600
//    val minutes = (timeInSeconds % 3600) / 60
//    val seconds = timeInSeconds % 60
//
//    Surface(
//        modifier = Modifier.fillMaxWidth().padding(16.dp),
//        color = MaterialTheme.colorScheme.surfaceVariant,
//        shape = MaterialTheme.shapes.medium,
//        shadowElevation = 4.dp
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth().padding(8.dp),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            InfoItem(title = "고도", value = "$altitude m")
//            InfoItem(title = "걸음 수", value = "$steps 걸음")
//            InfoItem(title = "움직인 거리", value = String.format("%.2f km", distance))
//            InfoItem(title = "경과 시간", value = String.format("%02d시 %02d분 %02d초", hours, minutes, seconds))
//        }
//    }
//}
//
//@Composable
//fun InfoItem(title: String, value: String) {
//    Column(
//        modifier = Modifier.padding(8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(text = title, style = MaterialTheme.typography.titleMedium)
//        Text(text = value, style = MaterialTheme.typography.bodyLarge)
//    }
//}
//
//data class WebSocketMessage(
//    val type: String,
//    val partyId: Int,
//    val userId: Int,
//    val userNickname: String,
//    val userProfile: String?,
//    val lat: String,
//    val lng: String
//)
//
//data class WebSocketSendMessage(
//    val type: String,
//    val lat: String,
//    val lng: String
//)
//
//data class UserLocationData(
//    val latitude: Double,
//    val longitude: Double
//)
//
//suspend fun getCurrentLocation(context: Context): UserLocationData? {
//    val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
//
//    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//        return null
//    }
//
//    return try {
//        val location = fusedLocationProviderClient.lastLocation.await()
//        if (location != null) {
//            UserLocationData(location.latitude, location.longitude)
//        } else {
//            null
//        }
//    } catch (e: Exception) {
//        Log.e("LocationError", "Error getting location", e)
//        null
//    }
//}

package com.example.testmap

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.testmap.ui.theme.TestmapTheme
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.*
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import com.google.gson.Gson
import com.naver.maps.map.overlay.OverlayImage
import java.io.ByteArrayOutputStream
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.Point
import org.locationtech.jts.operation.distance.DistanceOp

// API 인터페이스 정의
interface MountainService {
    @GET("mountain/v2/2")
    suspend fun getMountainData(): MountainResponse

    @GET("mountain/v2/442/all-course")
    suspend fun getAllCourses(): PathResponse
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
    var webSocket: WebSocket? = null
    private val webSocketUrl = "wss://k10e201.p.ssafy.io/api/hiking/chat/rooms/1"

    companion object {
        private const val REQUEST_CODE_ACTIVITY_RECOGNITION = 1
        private const val TAG = "MainActivity"
    }

    // 권한 요청을 위한 런처 선언
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    // 알림 권한 요청 함수
    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Firebase 초기화
        FirebaseApp.initializeApp(this)

        // 알림 권한 요청
        askNotificationPermission()

        // Firebase 초기화 및 FCM 토큰 가져오기
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })

        setContent {
            TestmapTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MapScreen(
                        context = this@MainActivity,
                        token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNyIsInVzZXJOaWNrbmFtZSI6IlRlc3RTU0FGWTU1NSIsImlhdCI6MTcxNTY2OTM3NiwiZXhwIjoxNzQ3MTk5Mzc2fQ.-9kQCi15rJaeKlVtFk40cxwjKXCvqJdGvp1GCLJ5ny8"
                    )
                }
            }
        }
    }

    fun startWebSocket(updateLocation: (String, Double, Double, String?) -> Unit, token: String) {
        val request = Request.Builder()
            .url(webSocketUrl)
            .addHeader("Authorization", "Bearer $token")
            .build()

        val listener = object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "Received message: $text")
                try {
                    val message = Gson().fromJson(text, WebSocketMessage::class.java)
                    updateLocation(message.userNickname, message.lat.toDouble(), message.lng.toDouble(), message.userProfile)
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

@ExperimentalNaverMapApi
@Composable
fun MapScreen(
    context: Context,
    token: String,
) {
    val mountainService = remember { createMountainService(token) }
    var mountainData by remember { mutableStateOf<MountainData?>(null) }
    var pathData by remember { mutableStateOf<List<CourseDetail>>(listOf()) }
    var userPositions by remember { mutableStateOf<Map<String, LatLng>>(mapOf()) }

    var stepCount by remember { mutableStateOf(0) }
    var distanceMoved by remember { mutableStateOf(0f) }
    var timerRunning by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableStateOf(0) }
    var altitude by remember { mutableStateOf(0f) }

    val userMarkerStates by remember { mutableStateOf<MutableMap<String, MarkerState>>(mutableMapOf()) }
    val userIcons by remember { mutableStateOf<MutableMap<String, OverlayImage?>>(mutableMapOf()) }
    val showAlert = remember { mutableStateOf(false) }

    val mainActivity = LocalContext.current as MainActivity

    LaunchedEffect(userPositions) {
        userPositions.forEach { (nickname, position) ->
            userMarkerStates[nickname] = MarkerState(position = position)
        }
    }

    LaunchedEffect(true) {
        try {
            val pathResponse = mountainService.getAllCourses()
            if (pathResponse.status == 200 && pathResponse.data.course.isNotEmpty()) {
                pathData = pathResponse.data.course
            } else {
                Log.d("MapScreen", "Failed to load courses or empty list: Status ${pathResponse.status}")
            }
        } catch (e: Exception) {
            Log.e("MapScreen", "Error fetching data", e)
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

    DisposableEffect(Unit) {
        val pressureSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_PRESSURE)
        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_PRESSURE) {
                    val pressure = event.values[0]
                    altitude = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressure)
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        pressureSensor?.let {
            sensorManager?.registerListener(sensorEventListener, it, SensorManager.SENSOR_DELAY_UI)
        }

        onDispose {
            sensorManager?.unregisterListener(sensorEventListener)
        }
    }

    LaunchedEffect(timerRunning) {
        if (timerRunning) {
            mainActivity.startWebSocket({ nickname, lat, lng, imageUrl ->
                userPositions = userPositions.toMutableMap().apply {
                    this[nickname] = LatLng(lat, lng)
                }
                if (!imageUrl.isNullOrEmpty()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val icon = getOverlayImageFromUrl(imageUrl, 100, 100)
                        withContext(Dispatchers.Main) {
                            userIcons[nickname] = icon
                        }
                    }
                }
            }, token)
            while (timerRunning) {
                delay(2000)
                elapsedTime += 2
                val currentLocation = getCurrentLocation(context)
                currentLocation?.let {
                    checkRouteDeviation(it.latitude, it.longitude, pathData) { isDeviated ->
                        showAlert.value = isDeviated
                    }
                    // Send location to WebSocket
                    val locationJson = Gson().toJson(WebSocketSendMessage(
                        type = "message",
                        lat = it.latitude.toString(),
                        lng = it.longitude.toString()
                    ))
                    mainActivity.webSocket?.send(locationJson)
                }
            }
        } else {
            mainActivity.stopWebSocket()
            elapsedTime = 0
            userMarkerStates.clear()
            userPositions = mapOf()
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
                val context = LocalContext.current
                val resizedIcon = remember { resizeMarkerIcon(context, R.drawable.custom_marker, 100, 100) }
                mountainData?.let {
                    Marker(
                        state = rememberMarkerState(position = LatLng(it.lat, it.lng)),
                        captionText = it.mountainName,
                        captionTextSize = 14.sp,
                        captionMinZoom = 12.0,
                        icon = resizedIcon
                    )
                }
                pathData.forEach { courseDetail ->
                    val path = courseDetail.validLocationDataList.map { LatLng(it.lat, it.lng) }
                    if (path.size >= 2) {
                        PathOverlay(
                            coords = path,
                            width = 3.dp,
                            color = Color.Green,
                            outlineWidth = 1.dp,
                            outlineColor = Color.Red,
                            tag = courseDetail.courseId
                        )
                    }
                }
                userMarkerStates.forEach { (nickname, markerState) ->
                    val icon = userIcons[nickname] ?: resizedIcon
                    Marker(
                        state = markerState,
                        captionText = nickname,
                        captionTextSize = 14.sp,
                        captionMinZoom = 12.0,
                        icon = icon
                    )
                }
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
            InfoPanel(altitude.toInt(), stepCount, distanceMoved, elapsedTime)
        }
//        if (showAlert.value) {
//            AlertDialog(
//                onDismissRequest = { showAlert.value = false },
//                title = { Text(text = "경고") },
//                text = { Text(text = "경로를 이탈했습니다!") },
//                confirmButton = {
//                    Button(onClick = { showAlert.value = false }) {
//                        Text("확인")
//                    }
//                }
//            )
//        }
    }
}

fun checkRouteDeviation(latitude: Double, longitude: Double, pathData: List<CourseDetail>, onDeviation: (Boolean) -> Unit) {
    val geometryFactory = GeometryFactory()
    val userLocation: Point = geometryFactory.createPoint(Coordinate(longitude, latitude))

    pathData.firstOrNull()?.let { courseDetail ->
        val coordinates = courseDetail.validLocationDataList.map { Coordinate(it.lng, it.lat) }.toTypedArray()
        val predefinedRoute: LineString = geometryFactory.createLineString(coordinates)
        val distanceOp = DistanceOp(predefinedRoute, userLocation)
        val minDistance = distanceOp.distance()
        val distanceInMeters = minDistance * 111319.9

        val isDeviated = distanceInMeters > 20.0
        onDeviation(isDeviated)

        if (!isDeviated) {
            Log.d("RouteCheck", "User is on the predefined route")
        }
    }
}

suspend fun downloadImage(url: String): Bitmap? {
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

fun getCircularBitmap(bitmap: Bitmap): Bitmap {
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

suspend fun getOverlayImageFromUrl(url: String, width: Int, height: Int): OverlayImage? {
    if (url == null || url.isEmpty()) {
        Log.e("ImageDownloadError", "Invalid URL: $url")
        return null
    }

    val bitmap = downloadImage(url)
    return bitmap?.let {
        val circularBitmap = getCircularBitmap(it)
        val resizedBitmap = Bitmap.createScaledBitmap(circularBitmap, width, height, false)
        val stream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        val bitmapResized = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        OverlayImage.fromBitmap(bitmapResized)
    }
}

fun resizeMarkerIcon(context: Context, drawableResId: Int, width: Int, height: Int): OverlayImage {
    val bitmap = BitmapFactory.decodeResource(context.resources, drawableResId)
    val circularBitmap = getCircularBitmap(bitmap)
    val resizedBitmap = Bitmap.createScaledBitmap(circularBitmap, width, height, false)

    val stream = ByteArrayOutputStream()
    resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    val byteArray = stream.toByteArray()
    val bitmapResized = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

    return OverlayImage.fromBitmap(bitmapResized)
}

@Composable
fun InfoPanel(altitude: Int, steps: Int, distance: Float, timeInSeconds: Int) {
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
            InfoItem(title = "고도", value = "$altitude m")
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
    val userId: Int,
    val userNickname: String,
    val userProfile: String?,
    val lat: String,
    val lng: String
)

data class WebSocketSendMessage(
    val type: String,
    val lat: String,
    val lng: String
)

data class UserLocationData(
    val latitude: Double,
    val longitude: Double
)

suspend fun getCurrentLocation(context: Context): UserLocationData? {
    val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        return null
    }

    return try {
        val location = fusedLocationProviderClient.lastLocation.await()
        if (location != null) {
            UserLocationData(location.latitude, location.longitude)
        } else {
            null
        }
    } catch (e: Exception) {
        Log.e("LocationError", "Error getting location", e)
        null
    }
}
