package com.santeut.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapType
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.naver.maps.map.compose.rememberMarkerState
import com.naver.maps.map.overlay.OverlayImage
import com.santeut.R
import com.santeut.data.model.request.StartHikingRequest
import com.santeut.data.model.request.WebSocketSendMessageRequest
import com.santeut.data.model.response.LocationDataResponse
import com.santeut.data.model.response.UserLocationDataResponse
import com.santeut.ui.mountain.MountainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.Point
import org.locationtech.jts.operation.distance.DistanceOp
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime

@SuppressLint("MutableCollectionMutableState")
@ExperimentalNaverMapApi
@Composable
fun HikingScreen(
    partyId: Int,
    hikingViewModel: HikingViewModel = hiltViewModel(),
    mountainViewModel: MountainViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val mountain by mountainViewModel.mountain.observeAsState()
    val courseList by hikingViewModel.courseList.observeAsState(emptyList())
    var userPositions by remember { mutableStateOf<Map<String, LatLng>>(mapOf()) }

    var stepCount by remember { mutableStateOf(0) }
    val distanceMoved by remember { mutableStateOf(0f) }
    var timerRunning by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableStateOf(0) }
    var altitude by remember { mutableStateOf(0f) }

    val userMarkerStates by remember { mutableStateOf<MutableMap<String, MarkerState>>(mutableMapOf()) }
    val userIcons by remember { mutableStateOf<MutableMap<String, OverlayImage?>>(mutableMapOf()) }
    val showAlert = remember { mutableStateOf(false) }
    val alertMessage = remember { mutableStateOf("") }

    LaunchedEffect(userPositions) {
        userPositions.forEach { (nickname, position) ->
            userMarkerStates[nickname] = MarkerState(position = position)
        }
    }

    LaunchedEffect(true) {
        hikingViewModel.startHiking(StartHikingRequest(partyId, LocalDateTime.now()))
    }

    val sensorManager = ContextCompat.getSystemService(context, SensorManager::class.java)
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
            sensorManager.registerListener(sensorEventListener, it, SensorManager.SENSOR_DELAY_UI)
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
                    altitude = SensorManager.getAltitude(
                        SensorManager.PRESSURE_STANDARD_ATMOSPHERE,
                        pressure
                    )
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        pressureSensor?.let {
            sensorManager.registerListener(sensorEventListener, it, SensorManager.SENSOR_DELAY_UI)
        }

        onDispose {
            sensorManager?.unregisterListener(sensorEventListener)
        }
    }

    LaunchedEffect(timerRunning) {
        if (timerRunning) {
            hikingViewModel.startWebSocket(partyId, { nickname, lat, lng, imageUrl ->
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
            }, showAlert, alertMessage)
            launch {
                while (timerRunning) {
                    delay(1000)
                    elapsedTime += 1
                }
            }
            launch {
                while (timerRunning) {
                    delay(2000)
                    val currentLocation = getCurrentLocation(context)
                    currentLocation?.let {
                        checkRouteDeviation(it.latitude, it.longitude, courseList) { isDeviated ->
                            showAlert.value = isDeviated
                            if (isDeviated) {
                                val locationJson = Gson().toJson(
                                    WebSocketSendMessageRequest(
                                        type = "offCourse",
                                        lat = it.latitude.toString(),
                                        lng = it.longitude.toString()
                                    )
                                )
                                hikingViewModel.webSocket?.send(locationJson)
                            }
                        }
                    }
                }
            }
        } else {
            hikingViewModel.stopWebSocket()
            elapsedTime = 0
            userMarkerStates.clear()
            userPositions = mapOf()
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(LatLng(35.8447943443487, 127.11199020254), 16.0)
    }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val location = getCurrentLocation(context)
            location?.let {
                cameraPositionState.position =
                    CameraPosition(LatLng(it.latitude, it.longitude), 16.0)
            }
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
                val resizedIcon =
                    remember { resizeMarkerIcon(context, R.drawable.logo, 100, 100) }
                mountain?.let {
                    Marker(
                        state = rememberMarkerState(position = LatLng(it.lat, it.lng)),
                        captionText = it.mountainName,
                        captionTextSize = 14.sp,
                        captionMinZoom = 12.0,
                        icon = resizedIcon
                    )
                }

                if (courseList.isNotEmpty()) {
                    val path = courseList.map { LatLng(it.lat, it.lng) }
                    if (path.size >= 2) {
                        PathOverlay(
                            coords = path,
                            width = 3.dp,
                            color = Color.Green,
                            outlineWidth = 1.dp,
                            outlineColor = Color.Red,
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
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Text(if (timerRunning) "중지" else "등산 시작")
            }
        }
        Box(modifier = Modifier.weight(2f)) {
            InfoPanel(altitude.toInt(), stepCount, distanceMoved, elapsedTime)
        }
        if (showAlert.value) {
            AlertDialog(
                onDismissRequest = { showAlert.value = false },
                title = { Text(text = "경고") },
                text = { Text(text = alertMessage.value) },
                confirmButton = {
                    Button(onClick = { showAlert.value = false }) {
                        Text("확인")
                    }
                }
            )
        }
    }
}

@Composable
fun InfoPanel(altitude: Int, steps: Int, distance: Float, timeInSeconds: Int) {
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
            InfoItem(title = "고도", value = "$altitude m")
            InfoItem(title = "걸음 수", value = "$steps 걸음")
            InfoItem(title = "움직인 거리", value = String.format("%.2f km", distance))
            InfoItem(
                title = "경과 시간",
                value = String.format("%02d시 %02d분 %02d초", hours, minutes, seconds)
            )
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

fun checkRouteDeviation(
    latitude: Double,
    longitude: Double,
    courseList: List<LocationDataResponse>,
    onDeviation: (Boolean) -> Unit
) {
    val geometryFactory = GeometryFactory()
    val userLocation: Point = geometryFactory.createPoint(Coordinate(longitude, latitude))

    val validLocations = LocationDataResponse.getValidLocations(courseList)
    if (validLocations.isNotEmpty()) {
        val coordinates = validLocations.map { Coordinate(it.lng, it.lat) }.toTypedArray()
        val predefinedRoute: LineString = geometryFactory.createLineString(coordinates)
        val distanceOp = DistanceOp(predefinedRoute, userLocation)
        val minDistance = distanceOp.distance()
        val distanceInMeters = minDistance * 111319.9

        val isDeviated = distanceInMeters > 20.0
        onDeviation(isDeviated)

        if (!isDeviated) {
            Log.d("RouteCheck", "User is on the predefined route")
        }
    } else {
        onDeviation(true)
        Log.d("RouteCheck", "No valid locations in course list")
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
    if (url.isEmpty()) {
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

suspend fun getCurrentLocation(context: Context): UserLocationDataResponse? {
    val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return null
    }

    return try {
        val location = fusedLocationProviderClient.lastLocation.await()
        if (location != null) {
            UserLocationDataResponse(location.latitude, location.longitude)
        } else {
            null
        }
    } catch (e: Exception) {
        Log.e("LocationError", "Error getting location", e)
        null
    }
}
