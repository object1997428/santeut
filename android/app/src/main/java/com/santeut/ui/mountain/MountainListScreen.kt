package com.santeut.ui.mountain

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
import com.santeut.R
import com.santeut.data.model.response.MountainResponse
import com.santeut.ui.home.SearchMountainBar
import com.santeut.ui.party.SelectedMountainCard

@Composable
fun MountainListScreen(
    type: String?,
    name: String, region: String?,
    navController: NavController,
    mountainViewModel: MountainViewModel = hiltViewModel()
) {
    val mountains by mountainViewModel.mountains.observeAsState(emptyList())

    LaunchedEffect(key1 = name) {
        mountainViewModel.searchMountain(name, region)
    }

    Column {
        if (type != "create") {
            SearchMountainBar(
                null,
                navController,
                onClickMap = {}
            )

            LazyColumn {
                items(mountains) { mountain ->
                    MountainCard(navController, mountain)
                }
            }
        } else {
            SearchMountainBar(
                "create",
                navController,
                onClickMap = {}
            )

            LazyColumn {
                items(mountains) { mountain ->
                    SelectedMountainCard(navController, mountain)
                }
            }
        }
    }
}

@Composable
fun MountainCard(navController: NavController, mountain: MountainResponse) {
    Card(
        modifier = Modifier
            .clickable(onClick = { navController.navigate("mountain/${mountain.mountainId}") })
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
