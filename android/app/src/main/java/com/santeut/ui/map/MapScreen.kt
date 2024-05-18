package com.santeut.ui.map

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapType
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@ExperimentalNaverMapApi
@Composable
fun MapScreen(
    mapViewModel: MapViewModel
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(LatLng(35.11549658154063, 129.00695075167815), 15.0)
    }

    val courseList by mapViewModel.courseList

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            Button(
                onClick = { mapViewModel.endedHiking() },
                modifier = Modifier
                    .height(40.dp)
                    .padding(16.dp)
                    .background(Color.Red),
            ) {
                Text(text = "종료 버튼")
            }

            NaverMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    locationTrackingMode = LocationTrackingMode.Follow,
                    mapType = MapType.Terrain,
                    isMountainLayerGroupEnabled = true
                )
            ){
                // 등산 코스 경로
                if(courseList.isNotEmpty()) {
                    PathOverlay(
                        coords = courseList,
                        width = 3.dp,
                        color = Color.Green,
                        outlineWidth = 1.dp,
                        outlineColor = Color.Red,
                    )
                }
            }
        }
    }
}
