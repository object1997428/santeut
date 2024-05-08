package com.santeut.ui.community.guild

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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.santeut.data.model.response.GuildResponse
import com.santeut.ui.guild.GuildViewModel

@Composable
fun JoinGuildScreen(
    guildViewModel: GuildViewModel = hiltViewModel()
) {
    val guilds by guildViewModel.guilds.observeAsState(initial = emptyList())
    LazyColumn {
        items(guilds) { guild ->
            GuildCard(guild)
        }
    }
}

@Composable
fun GuildCard(guild: GuildResponse) {

    val region = when (guild.guildId) {
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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp,
    ) {
        Box(
            modifier = Modifier.height(150.dp),
        ) {
            Row {

                AsyncImage(
                    model = guild.guildProfile,
                    contentDescription = "동호회 사진"
                )

                Column {
                    Text(text = guild.guildName)
                    Text(text = "${guild.guildMember}명")
                    Text(text = region)
                }
            }
        }
    }
}