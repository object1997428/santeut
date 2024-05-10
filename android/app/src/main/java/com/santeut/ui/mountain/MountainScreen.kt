package com.santeut.ui.mountain

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.santeut.R
import com.santeut.data.model.response.MountainDetailResponse
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun MountainScreen(
    mountainId: Int,
    mountainViewModel: MountainViewModel = hiltViewModel()
) {

    val pages = listOf("등산 코스", "날씨")
    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()

    val mountain by mountainViewModel.mountain.observeAsState()

    LaunchedEffect(key1 = mountainId) {
        mountainViewModel.mountainDetail(mountainId)
    }

    Scaffold() {
        Column(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = mountain?.image ?: R.drawable.logo,
                contentDescription = "산 이미지",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            MountainDetail(mountain)

            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth()
                    .background(Color.Black)
            ) {
                pages.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }

            HorizontalPager(
                count = pages.size,
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                when (page) {
                    0 -> HikingCourse(mountain)
                    1 -> MountainWeather(mountain)
                }
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
                    text = mountain.mountainName?:"",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "${mountain.height?:0}m",
                    modifier = Modifier.padding(8.dp).align(Alignment.Bottom)
                )
            }
            Text(
                text = mountain.address?:"",
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = mountain.description?:"",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun HikingCourse(mountain: MountainDetailResponse?) {

    /* 등산 코스 지도 자리 */
    if (mountain != null) {
        Column {
            Row {
                Text(text = "등산로")
                Text(text = "${mountain.courseCount?:0}개")
            }

            LazyColumn() {
                items(mountain.courseCount) { course ->
                    CourseItem()
                }
            }
        }
    }
}

@Composable
fun CourseItem() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "코스 번호: ")
            Text(text = "코스 이름: ")
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "상행 시간:")
            Text(text = "하행 시간: ")
            Text(text = "길이: ")
        }
    }
}

@Composable
fun MountainWeather(mountain: MountainDetailResponse?) {
    Text(text = "날씨 정보")
}