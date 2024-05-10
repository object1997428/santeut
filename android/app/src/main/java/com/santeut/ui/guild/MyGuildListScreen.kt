package com.santeut.ui.guild

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.santeut.R
import com.santeut.data.model.response.GuildResponse

@Composable
fun MyGuildListScreen(
    navController: NavController,
    guildViewModel: GuildViewModel = hiltViewModel()
) {
    val guilds by guildViewModel.guilds.observeAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        guildViewModel.myGuilds()
    }
    if (guilds.isEmpty()) {
        // 동호회 목록이 emptyList()인 경우
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "아직 동호회에 가입하지 않았습니다.",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        LazyColumn {
            items(guilds) { guild ->
                GuildCard(guild, navController)
            }
        }
    }
}

@Composable
fun GuildCard(guild: GuildResponse, navController: NavController) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                navController.navigate("getGuild/${guild.guildId}")
            },
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp,
    ) {
        Box(
            modifier = Modifier.height(150.dp),
        ) {
            Row {

                AsyncImage(
                    model = guild.guildProfile ?: R.drawable.logo,
                    contentDescription = "동호회 사진"
                )

                Column {
                    androidx.compose.material.Text(text = guild.guildName)
                    androidx.compose.material.Text(text = "${guild.guildMember ?: 0}명")
                    androidx.compose.material.Text(text = regionName(guild.regionId))
                }
            }
        }
    }
}

fun regionName(regionId: Int): String {
    return when (regionId) {
        0 -> "전체"
        1 -> "서울"
        2 -> "부산"
        3 -> "대구"
        4 -> "인천"
        5 -> "광주"
        6 -> "대전"
        7 -> "울산"
        8 -> "세종"
        9 -> "경기"
        10 -> "충북"
        11 -> "충남"
        12 -> "전북"
        13 -> "전남"
        14 -> "경북"
        15 -> "경남"
        16 -> "제주"
        17 -> "강원"
        else -> "기타"
    }
}