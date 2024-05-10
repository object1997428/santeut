package com.santeut.ui.mountain

import android.graphics.Color.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.santeut.R
import com.santeut.data.model.response.MountainResponse
import com.santeut.ui.home.HomeSearchBar
import kotlinx.coroutines.launch

@Composable
fun MountainScreen(
    mountainId: Int,
    mountainViewModel: MountainViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        FirstColumn(modifier = Modifier.weight(2f))
        SecondColumn(modifier = Modifier.weight(3f))
        ThirdColumn(modifier = Modifier.weight(3f))
        FourthColumn(modifier = Modifier.weight(2f))
    }
}

@Composable
fun FirstColumn(modifier: Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text("First cl", modifier = Modifier.fillMaxHeight().padding(8.dp))
    }
}

@Composable
fun SecondColumn(modifier: Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        // 첫 번째 컬럼
        Row(modifier = Modifier.weight(2f)) {
            Text("산 이름: 백두산", modifier = Modifier.weight(6f).padding(8.dp))
            Text("해발 고도: 2744m", modifier = Modifier.weight(4f).padding(8.dp))
        }
        // 두 번째 컬럼
        Row(modifier = Modifier.weight(2f)) {
            Text("주소: 어딘가의 주소", modifier = Modifier.weight(7f).padding(8.dp))
            Text("코스 수: 5", modifier = Modifier.weight(3f).padding(8.dp))
        }
        // 세 번째 컬럼
        Row(modifier = Modifier.weight(6f)) {
            Text("산에 대한 설명이 여기에 포함됩니다. 백두산은 한반도의 가장 높은 산이며, 중국과의 국경에 위치해 있습니다. 많은 역사적, 문화적 의미를 갖고 있으며, 아름다운 경관을 자랑합니다.", modifier = Modifier.fillMaxWidth().padding(8.dp))
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ThirdColumn(modifier: Modifier) {
    val pages = listOf("등산코스", "날씨")
    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxWidth()) {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            edgePadding = 0.dp
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
            modifier = Modifier.weight(5f)
        ) { page ->
            when (page) {
                0 -> Text("등산코스 정보: 여기에 등산코스에 대한 상세 정보를 제공합니다.", textAlign = TextAlign.Center, modifier = Modifier.fillMaxSize().padding(16.dp))
                1 -> Text("날씨 정보: 여기에 현재 위치의 날씨 정보를 표시합니다.", textAlign = TextAlign.Center, modifier = Modifier.fillMaxSize().padding(16.dp))
            }
        }
    }
}

// 임시 데이터 생성
val sampleCourses = listOf(
    CourseData("001", "코스 A", "1시간", "50분", "3.5km"),
    CourseData("002", "코스 B", "2시간", "1시간 30분", "5km")
)

data class CourseData(
    val id: String,
    val name: String,
    val upTime: String,
    val downTime: String,
    val length: String
)

@Composable
fun FourthColumn(modifier: Modifier) {
    LazyColumn(modifier = modifier) {
        items(sampleCourses) { course ->
            CourseItem(course = course)
        }
    }
}

@Composable
fun CourseItem(course: CourseData) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "코스 번호: ${course.id}")
            Text(text = "코스 이름: ${course.name}")
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "상행 시간: ${course.upTime}")
            Text(text = "하행 시간: ${course.downTime}")
            Text(text = "길이: ${course.length}")
        }
    }
}
