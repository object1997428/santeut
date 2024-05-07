@file:OptIn(ExperimentalNaverMapApi::class)

package com.santeut.ui.map

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
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

    // 위치 추적 모드를 기본값으로 설정 (이전에 선택한 모드 사용 버튼 제거 후)
    val locationTrackingMode = LocationTrackingMode.Follow

    // 지도의 카메라 위치 상태 관리
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(LatLng(37.5666102, 126.9783881), 14.0)
    }

    LaunchedEffect(key1 = currentLocation) {
        currentLocation?.let {
            cameraPositionState.position = CameraPosition(it, 15.0)
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
                    locationTrackingMode = locationTrackingMode,
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
