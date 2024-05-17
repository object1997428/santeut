@file:OptIn(ExperimentalNaverMapApi::class)

package com.santeut.ui.mountain

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
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
import com.santeut.data.model.response.CourseDetailRespnse
import com.santeut.data.model.response.HikingCourseResponse
import com.santeut.data.model.response.MountainDetailResponse

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun MountainScreen(
    mountainId: Int,
    mountainViewModel: MountainViewModel = hiltViewModel()
) {

    val pages = listOf("등산 코스", "날씨")
    var selectedTab by remember { mutableIntStateOf(0) } // 탭 상태 관리

    val mountain by mountainViewModel.mountain.observeAsState()
    val courseList by mountainViewModel.courseList.observeAsState(emptyList())
    val pathData by mountainViewModel.pathList.observeAsState(listOf())


    LaunchedEffect(key1 = mountainId) {
        mountainViewModel.mountainDetail(mountainId)
        mountainViewModel.getHikingCourseList(mountainId)
        mountainViewModel.setPathList(mountainId)
    }

    Scaffold {

        // 전체 스크롤 또는 정보는 고정하고 TabRow Content만 스크롤
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            item {
                AsyncImage(
                    model = mountain?.image ?: R.drawable.logo,
                    contentDescription = "산 이미지",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }

            item { MountainDetail(mountain) }

            item {
                TabRow(
                    selectedTabIndex = selectedTab
                ) {
                    pages.forEachIndexed { index, title ->
                        Tab(
                            text = { Text(title) },
                            selected = selectedTab == index,
                            onClick = { selectedTab = index }
                        )
                    }
                }
            }

            when (selectedTab) {
                0 -> item { HikingCourse(mountain?.courseCount ?: 0, courseList, pathData) }
                1 -> item { MountainWeather(mountain) }
            }
        }
    }
}

@Composable
fun MountainDetail(mountain: MountainDetailResponse?) {
    if (mountain != null) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row {
                Text(
                    text = mountain.mountainName ?: "",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "${mountain.height ?: 0}m",
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.Bottom)
                )
            }
            Text(
                text = mountain.address ?: "",
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = mountain.description ?: "",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun HikingCourse(
    courseCount: Int,
    courseList: List<HikingCourseResponse>,
    pathData: List<CourseDetailRespnse>
) {

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(LatLng(35.116824651798, 128.99110450587247), 15.0)
    }
    Column(
        modifier = Modifier.padding(20.dp) // 전체 Column에 패딩 적용
    ) {

        Box(
            modifier = Modifier
                .height(232.dp)
                .fillMaxWidth()
                .background(Color(0xFFD9D9D9))
                .shadow(4.dp, RoundedCornerShape(4.dp)),
        ) {
            NaverMap(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    locationTrackingMode = LocationTrackingMode.Follow,
                    mapType = MapType.Terrain,
                    isMountainLayerGroupEnabled = true
                )
            ) {
                pathData.forEach { courseDetail ->
                    val path = courseDetail.locationDataList.map { LatLng(it.lat, it.lng) }
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
            }
        }

        Column {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Text(
                    text = "등산로",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp,
                    fontFamily = FontFamily.SansSerif
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${courseCount}개",
                    color = Color.Black,
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    textAlign = TextAlign.Right,
                )
            }

            courseList.forEach { course ->
                CourseItem(
                    course = course,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

        }
    }
}

@Composable
fun CourseItem(course: HikingCourseResponse, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                color = Color(0xFFE5DD90),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(8.dp) // Internal padding
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .background(color = Color(0xFFE5DD90))
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${course.courseName ?: ""} 코스",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Gray)) {
                                append("난이도 ")
                            }
                            // "Level" value in a different color, if present
                            withStyle(style = SpanStyle(color = getColorForLevel(course.level))) {
                                append(course.level ?: "알 수 없음")
                            }
                        }
                    )
                }

                Row(
                    modifier = Modifier
                        .background(color = Color(0xFFE5DD90))
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Gray)) {
                                append("거리 ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Black,
                                )
                            ) {
                                append("${course.distance ?: "?"}km")
                            }
                        }
                    )
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Gray)) {
                                append("등산 시간 ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Black,
                                )
                            ) {
                                append("${course.upTime ?: "?"}분")
                            }
                        }
                    )
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Gray)) {
                                append("하산 시간 ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Black,
                                )
                            ) {
                                append("${course.downTime ?: "?"}분")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun getColorForLevel(level: String?): Color {
    return when (level) {
        "쉬움" -> Color(0xFF335C49)  // Dark green
        "보통" -> Color.Black
        "어려움" -> Color.Red
        else -> Color.Gray
    }
}

@Composable
fun MountainWeather(mountain: MountainDetailResponse?) {
    Text(text = "날씨 정보")
}