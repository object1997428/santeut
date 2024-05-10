package com.santeut.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import com.santeut.ui.health.HealthScreenState

@Composable
fun MapScreen(
    uiState: HealthScreenState
) {
    val latitude = uiState.exerciseState?.exerciseMetrics?.location?.latitude
    val longitude = uiState.exerciseState?.exerciseMetrics?.location?.longitude

//        val singapore = LatLng(latitude, longitude)
        val singapore = LatLng(35.093655296844, 128.85567741474)

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(singapore, 12f)
        }

        val mapProperties = MapProperties(
            mapType = MapType.TERRAIN,
            mapStyleOptions = MapStyleOptions(mapStyleJson)
        )

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false),
            properties = mapProperties
        ) {
            Marker(
                state = MarkerState(position = singapore),
                title = "Me",
                icon = BitmapDescriptorFactory.fromResource(R.drawable.map_marker_blue)
            )
        }

//    Column(
//        modifier = Modifier
//            .fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(text = "등산을 시작해 주세요!")
//    }
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