package com.santeut.ui.mountain

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.santeut.R
import com.santeut.data.model.response.MountainResponse
import com.santeut.ui.home.HomeSearchBar

@Composable
fun MountainListScreen(
    name: String, region: String?,
    navController: NavController,
    mountainViewModel: MountainViewModel = hiltViewModel()
) {

    val mountains by mountainViewModel.mountains.observeAsState(emptyList())

    LaunchedEffect(key1 = name) {
        mountainViewModel.searchMountain(name, region)
    }

    Column {
        HomeSearchBar(
            navController,
            onSearchTextChanged = {},
            onClickSearch = { },
            onClickMap = {}
        )

        LazyColumn {
            items(mountains) { mountain ->
                MountainCard(mountain)
            }
        }
    }
}

@Composable
fun MountainCard(mountain: MountainResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        AsyncImage(
            model = mountain.image ?: R.drawable.logo,
            contentDescription = "산 사진"
        )

        Row {
            Text(text = mountain.mountainName)
            Text(text = "${mountain.height}m")
        }
        Text(text = mountain.regionName)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween // 가로 방향으로 공간을 균등하게 분배하여 오른쪽 정렬
        ) {
            Text(text = "${mountain.courseCount}개 코스")
            if (mountain.isTop100) {
                Box(modifier = Modifier.background(color = Color.Green)) {
                    Text(text = "100대 명산")
                }
            }
        }
    }
}