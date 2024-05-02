@file:OptIn(ExperimentalNaverMapApi::class)

package com.ssafy.santeut.ui.map

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.*

@Composable
fun MapScreen(context: Context) {
    // 위치 권한 상태 관리
    var hasLocationPermission by remember { mutableStateOf(false) }

    // 위치 권한 요청
    val locationPermissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasLocationPermission = isGranted
        }
    )

    val fusedLocationClient: FusedLocationProviderClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // 앱 시작 시 위치 권한 요청
    LaunchedEffect(key1 = true) {
        locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }


    // 현재 위치 데이터
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }

    // 권한이 있을 때 현재 위치를 가져오기
    LaunchedEffect(key1 = hasLocationPermission) {
        if (hasLocationPermission) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    currentLocation = LatLng(location.latitude, location.longitude)
                }
            }
        }
    }

    val uiSettings = remember {
        MapUiSettings(
            isZoomControlEnabled = true,
            isLocationButtonEnabled = true,
            isCompassEnabled = true,
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            NaverMap(
                modifier = Modifier.fillMaxSize(),
                properties = MapProperties(mapType = MapType.Basic),
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
