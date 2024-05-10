package com.example.testpositions

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.Composable
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.*
import android.Manifest
import android.content.Context
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.location.*

@ExperimentalNaverMapApi
@Composable
fun MapScreen(context: Context) {
    val mainViewModel: MainViewModel = viewModel()
    var hasLocationPermission by remember { mutableStateOf(false) }
    val locationPermissionRequest = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
        if (isGranted) {
            mainViewModel.connectWebSocket()
        }
    }

    LaunchedEffect(key1 = true) {
        locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    val fusedLocationClient: FusedLocationProviderClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(LatLng(37.5666102, 126.9783881), 14.0)
    }

    if (hasLocationPermission) {
        LaunchedEffect(fusedLocationClient) {
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let { location ->
                        currentLocation = LatLng(location.latitude, location.longitude)
                    }
                }
            }
            fusedLocationClient.requestLocationUpdates(
                LocationRequest.create().apply {
                    interval = 1000
                    fastestInterval = 500
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                },
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    LaunchedEffect(key1 = currentLocation) {
        currentLocation?.let { loc ->
            cameraPositionState.move(CameraUpdate.toCameraPosition(CameraPosition(loc, 16.0)))
            mainViewModel.sendLocationMessage(loc.latitude, loc.longitude)
        }
    }

    DisposableEffect(key1 = Unit) {
        onDispose {
            mainViewModel.disconnectWebSocket()
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
        Box(modifier = Modifier.weight(1f)) {
            NaverMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                locationSource = rememberFusedLocationSource(isCompassEnabled = uiSettings.isCompassEnabled),
                properties = MapProperties(
                    locationTrackingMode = LocationTrackingMode.Follow,
                    mapType = MapType.Terrain
                ),
                uiSettings = uiSettings
            ) {
                currentLocation?.let {
                    Marker(
                        state = rememberMarkerState(position = it),
                        captionText = "현재 위치",
                        captionTextSize = 14.sp,
                        captionMinZoom = 12.0
                    )
                }
            }
        }
    }
}
