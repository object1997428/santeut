package com.santeut.ui.party

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapType
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.rememberCameraPositionState
import com.santeut.R
import com.santeut.data.model.request.CreatePartyRequest
import com.santeut.data.model.response.MountainResponse
import com.santeut.designsystem.theme.Green
import com.santeut.ui.home.SearchMountainBar
import com.santeut.ui.mountain.MountainViewModel
import com.santeut.ui.navigation.top.SimpleTopBar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun SelectedMountain(
    guildId: Int?,
    navController: NavController,
    onClickMap: () -> Unit,
) {
    Column {
        SearchMountainBar(type = "create", navController, onClickMap)
    }
}

@Composable
fun SelectedMountainCard(
    navController: NavController, mountain: MountainResponse
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .fillMaxWidth(),
        contentAlignment = Alignment.Center)
    {
        Card(
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .padding(16.dp)
                .clickable(onClick = { navController.navigate("create/courseList/${mountain.mountainId}/${mountain.mountainName}") }),
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
            ) {
                // 이미지 부분
                AsyncImage(
                    model = mountain.image ?: R.drawable.logo,
                    contentDescription = "산 사진",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Crop  // 이미지 채우기 방식
                )
                // 텍스트 정보 부분
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                ) {
                    // 산 이름
                    Text(text = mountain.mountainName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // 높이
                    Text(
                        text = "${mountain.height}m",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    // 지역
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = mountain.regionName,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // 코스 개수, 명산?
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 18.dp, top = 0.dp, bottom = 16.dp)
//                        .padding(vertical = 4.dp, horizontal = 8.dp)
                ) {
                    Text(
                        text = "${mountain.courseCount}개 코스",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    if (mountain.isTop100) {
                        Box(
                            modifier = Modifier
                                .background(color = Green, shape = RoundedCornerShape(10.dp))
                                .padding(vertical = 4.dp, horizontal = 8.dp)
                        ) {
                            Text(
                                text = "100대 명산",
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun SelectedCourse(
    mountainId: Int,
    mountainName: String,
    navController: NavController,
    mountainViewModel: MountainViewModel = hiltViewModel()
) {

    val pathList by mountainViewModel.pathList.observeAsState(emptyList())

    val selectedCourseIds = remember { mutableStateListOf<Int?>() }

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(pathList) {
        if (pathList.isNotEmpty() && pathList[0].locationDataList.isNotEmpty()) {
            cameraPositionState.position = CameraPosition(
                LatLng(
                    pathList[0].locationDataList[0].lat,
                    pathList[0].locationDataList[0].lng
                ), 15.0
            )
        }
    }

    LaunchedEffect(key1 = null) {
        mountainViewModel.setPathList(mountainId)
    }

    Scaffold(
        topBar = { SimpleTopBar(navController, "$mountainName 정보") },
        content = { paddingValues ->
            NaverMap(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(vertical = 8.dp),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    locationTrackingMode = LocationTrackingMode.Follow,
                    mapType = MapType.Terrain,
                    isMountainLayerGroupEnabled = true
                )
            ) {
                pathList.forEach { courseDetail ->
                    val path = courseDetail.locationDataList.map { LatLng(it.lat, it.lng) }
                    if (path.size >= 2) {
                        val isSelected = courseDetail.courseId in selectedCourseIds

                        PathOverlay(
                            coords = path,
                            width = 3.dp,
                            color = if (isSelected) Color.Red else Color.Green,
                            outlineWidth = 1.dp,
                            outlineColor = Color.Red,
                            tag = courseDetail.courseId,
                            onClick = {
                                if (isSelected) {
                                    selectedCourseIds.add(courseDetail.courseId)
                                } else {
                                    selectedCourseIds.add(courseDetail.courseId)
                                }
                            }
                        )
                    }
                }
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Text(text = "등산로를 선택해주세요")
                    Button(onClick = {
                        val selectedCourses = selectedCourseIds.joinToString(",")
                        Log.d("등산로", selectedCourses)
                        navController.navigate("createParty/${mountainId}/${selectedCourses}")
                    }) {
                        Text(text = "선택완료")
                    }
                }
            }
        }
    )
}