package com.santeut.ui.guild

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.santeut.R
import com.santeut.data.model.response.GuildMemberResponse
import com.santeut.data.model.response.RankingResponse

@Composable
fun GuildRankingScreen(
    guildViewModel: GuildViewModel = hiltViewModel()
) {
    val rankingList by guildViewModel.rankingList.observeAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = null) {
        guildViewModel.getRanking('H') // 최고고도
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                guildViewModel.getRanking('H') // 최고고도
            }) {
                Text("최고고도")
            }
            Button(onClick = {
                guildViewModel.getRanking('D') // 최장거리
            }) {
                Text("최장거리")
            }
            Button(onClick = {
                guildViewModel.getRanking('C') // 최다등반
            }) {
                Text("최다등반")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        rankingList?.let { rankingList ->
            if (rankingList.isEmpty()) {
                Text("랭킹이 존재하지 않습니다.")
            } else {
                rankingList.forEach { rank ->
                    RankingItem(rank)
                }
            }
        } ?: run {
            Text("Loading...")
        }
    }
}

@Composable
fun RankingItem(rank: RankingResponse) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(text = "${rank.order}", modifier = Modifier.weight(1f))
        AsyncImage(model = rank.userProfile ?: R.drawable.logo, contentDescription = "프로필 사진")
        Text(text = rank.userNickname, modifier = Modifier.weight(3f))
        Text(text = rank.score, modifier = Modifier.weight(2f))
    }
}
