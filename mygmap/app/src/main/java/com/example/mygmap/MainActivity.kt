package com.example.mygmap

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.example.mygmap.ui.theme.MygmapTheme
import com.google.accompanist.permissions.PermissionStatus

import androidx.compose.foundation.layout.*
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MygmapTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MapScreen()
                }
            }
        }
    }
}

@ExperimentalPermissionsApi
@Composable
fun MapScreen() {
    val context = LocalContext.current
    var location by remember { mutableStateOf<LatLng?>(null) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location ?: LatLng(0.0, 0.0), 17f)
    }
    val locationPermissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    LaunchedEffect(key1 = true) {
        locationPermissionState.launchPermissionRequest()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            cameraPositionState = cameraPositionState
        ) {
            location?.let {
                Marker(state = MarkerState(position = it))
            }
        }
        Button(
            onClick = {
                if (locationPermissionState.status is PermissionStatus.Granted) {
                    val locationRequest = LocationRequest.create().apply {
                        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                        interval = 10000  // 10초
                        fastestInterval = 5000  // 5초
                    }
                    val locationCallback = object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            val safeLocation = locationResult.lastLocation?.let {
                                LatLng(it.latitude, it.longitude)
                            }
                            safeLocation?.let {
                                location = it
                                cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
                            }
                        }
                    }
                    // 권한 다시 확인 또는 SecurityException 처리
                    try {
                        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
                        }
                    } catch (e: SecurityException) {
                        // 앱 실행 중 사용자가 권한을 취소하는 경우 처리
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = locationPermissionState.status is PermissionStatus.Granted
        ) {
            Text("내 위치 찾기")
        }
    }
}
