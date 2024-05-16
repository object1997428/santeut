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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.santeut.data.model.response.GuildMemberResponse
import com.santeut.data.model.response.GuildResponse
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
                        text = "동호회 회원",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "${applyList.size}명",
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

    var showDialog by remember { mutableStateOf(false) }

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
        Column {

            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = apply.userProfile ?: R.drawable.logo,
                    contentDescription = "회원 프로필 사진",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                )
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(
                        text = apply.userNickname,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = apply.createdAt,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            Row {
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "거절하기")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "승인하기")
                }
            }
        }
    }
}