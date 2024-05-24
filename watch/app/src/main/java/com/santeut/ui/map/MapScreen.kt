package com.santeut.ui.map

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.santeut.R
import com.santeut.ui.HealthDataViewModel
import com.santeut.ui.health.HealthScreenState

@Composable
fun MapScreen(
    healthDataViewModel: HealthDataViewModel,
    state: Boolean,
    uiState: HealthScreenState
) {
    val latitude = uiState.exerciseState?.exerciseMetrics?.location?.latitude ?: 0.0
    val longitude = uiState.exerciseState?.exerciseMetrics?.location?.longitude ?: 0.0

    val markerState = remember { MarkerState(position = LatLng(latitude, longitude)) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), 15f)
    }

    LaunchedEffect(state) {
        if (state == false) {
            healthDataViewModel.initUserPositions()
        }
    }

    LaunchedEffect(key1 = latitude, key2 = longitude) {
        markerState.position = LatLng(latitude, longitude)
        cameraPositionState.position =
            CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), 13f)
        Log.d("Map Screen LatLng : ", "$latitude / $longitude")
    }

    val userPositions by healthDataViewModel.userPositions

    val mapProperties = MapProperties(
        mapType = MapType.TERRAIN,
        mapStyleOptions = MapStyleOptions(mapStyleJson)
    )

    if (!state || ((latitude == 0.0 || longitude == 0.0) && userPositions.isEmpty())) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF335C49)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            androidx.wear.compose.material.Text(
                text = "지도",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(0.4f))
            androidx.wear.compose.material.Text(text = "등산을")
            androidx.wear.compose.material.Text(text = "시작해 주세요.")
            Spacer(modifier = Modifier.weight(0.6f))
        }
    } else {
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF335C49)),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false),
            properties = mapProperties
        ) {
            userPositions.forEach { (userNickname, position) ->
                Marker(
                    state = MarkerState(position = position),
                    title = userNickname,
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.map_marker_blue)
                )
            }
        }
    }
}

val mapStyleJson = """
[
  {
    "featureType": "all",
    "elementType": "labels",
    "stylers": [
      { "visibility": "off" }
    ]
  }
]
    """.trimIndent()