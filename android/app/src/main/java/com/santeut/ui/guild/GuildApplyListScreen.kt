package com.santeut.ui.guild

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.santeut.R
import com.santeut.data.model.response.GuildApplyResponse
import com.santeut.data.model.response.GuildResponse
import com.santeut.ui.community.tips.formatTime
import com.santeut.ui.navigation.top.GuildTopBar

@Composable
fun GuildApplyListScreen(
    guildId: Int,
    navController: NavController,
    guildViewModel: GuildViewModel = hiltViewModel()
) {

    val guild by guildViewModel.guild.observeAsState()
    val applyList by guildViewModel.applyList.observeAsState(emptyList())

    LaunchedEffect(key1 = guildId) {
        guildViewModel.getGuild(guildId)
        guildViewModel.getGuildApplyList(guildId)
    }

    Scaffold(
        topBar = {
            guild?.let { guild ->
                GuildTopBar(navController, guild)
            }
        },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "가입 요청",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "${applyList.size}건",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                LazyColumn(modifier = Modifier.padding(padding)) {
                    items(applyList) { apply ->
                        guild?.let { ApplyRow(it, apply) }
                    }
                }
            }
        }
    )

}

@Composable
fun ApplyRow(
    guild: GuildResponse,
    apply: GuildApplyResponse,
    guildViewModel: GuildViewModel = hiltViewModel()
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = apply.userProfile ?: R.drawable.logo,
                contentDescription = "회원 프로필 사진",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
            )
            Spacer(Modifier.width(16.dp))
            Column(
                modifier = Modifier.fillMaxWidth() // 공간을 적절히 분배하기 위해 추가
            ) {
                Text(
                    text = apply.userNickname,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
                Text(
                    text = formatTime(apply.createdAt),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }

        Row {
            Button(onClick = { guildViewModel.denyMember(guild.guildId, apply.userId) }) {
                Text(text = "거절하기")
            }
            Spacer(Modifier.width(8.dp))
            Button(onClick = { guildViewModel.approveMember(guild.guildId, apply.userId) }) {
                Text(text = "승인하기")
            }
        }
    }
}