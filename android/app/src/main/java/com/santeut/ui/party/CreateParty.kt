package com.santeut.ui.party

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMap
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapType
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.rememberCameraPositionState
import com.santeut.R
import com.santeut.data.model.response.MountainResponse
import com.santeut.ui.home.SearchMountainBar
import com.santeut.ui.mountain.MountainViewModel
import com.santeut.ui.navigation.top.SimpleTopBar

@Composable
fun CreateParty(
    guildId: Int?,
    navController: NavController,
    onClickMap: () -> Unit,
    mountainViewModel: MountainViewModel = hiltViewModel()
) {
    SearchMountainBar(type = "create", navController, onClickMap)
}

@Composable
fun SelectedMountainCard(
    navController: NavController, mountain: MountainResponse
) {
    Card(
        modifier = Modifier
            .clickable(onClick = { navController.navigate("create/courseList/${mountain.mountainId}/${mountain.mountainName}") })
            .fillMaxWidth()
            .height(100.dp)  // 카드 높이 조정
            .padding(8.dp),  // 카드 주변 여백
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 이미지 부분
            AsyncImage(
                model = mountain.image ?: R.drawable.logo,
                contentDescription = "산 사진",
                modifier = Modifier
                    .size(100.dp),  // 이미지 크기 고정
                contentScale = ContentScale.Crop  // 이미지 채우기 방식
            )

            Spacer(modifier = Modifier.width(16.dp))  // 이미지와 텍스트 사이 간격

            // 텍스트 정보 부분
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)  // 텍스트 내부 여백
                    .weight(1f),  // 남은 공간 모두 사용
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = mountain.mountainName,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${mountain.height}m",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = mountain.regionName,
                    style = MaterialTheme.typography.bodySmall
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "${mountain.courseCount}개 코스",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (mountain.isTop100) {
                        Box(
                            modifier = Modifier
                                .background(color = Color.Green, shape = RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "100대 명산",
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium
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

    val courseList by mountainViewModel.courseList.observeAsState(emptyList())
    val pathData by mountainViewModel.pathList.observeAsState(emptyList())
    val courseId = 1    // 선택한 코스로 입력


    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(LatLng(35.116824651798, 128.99110450587247), 15.0)
    }

    Scaffold(
        topBar = { SimpleTopBar(navController, mountainName) },
        content = { paddingValues ->
            // 해당 코스 선택 시 색상 변경
            NaverMap(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // 패딩 값을 여기서 전달
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
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = {
                    // 버튼 클릭 시 소모임 만들기 페이지 이동
                    // 이동 시, 산 아이디, 코스 아이디 넘겨주기
                    navController.navigate("createParty/${mountainId}/${courseId}")
                }) {
                    Text(text = "Button")
                }
            }
        }
    )
}

@Composable
fun InputPartyInfo(mountainId: Int, courseId: Int) {
    // CreatePartyScreen 값 가져올 것

}