package com.santeut.ui.guild

import android.annotation.SuppressLint
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
import com.santeut.data.model.response.GuildMemberResponse
import com.santeut.data.model.response.GuildResponse
import com.santeut.ui.navigation.top.GuildTopBar
import java.time.format.TextStyle

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GuildMemberListScreen(
    guildId: Int,
    navController: NavController,
    guildViewModel: GuildViewModel = hiltViewModel()
) {

    val guild by guildViewModel.guild.observeAsState()
    val memberList by guildViewModel.memberList.observeAsState(emptyList())

    LaunchedEffect(key1 = guildId) {
        guildViewModel.getGuild(guildId)
        guildViewModel.getGuildMemberList(guildId)
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
                        text = "${memberList.size}명",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                LazyColumn(modifier = Modifier.padding(padding)) {
                    items(memberList) { member ->
                        guild?.let { MemberRow(it, member) }
                    }
                }
            }
        }
    )

}

@Composable
fun MemberRow(
    guild: GuildResponse,
    member: GuildMemberResponse,
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = member.userProfile ?: R.drawable.logo,
                contentDescription = "회원 프로필 사진",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = member.userNickname,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )

            // 동호회 회장인 경우 추방하기 버튼 활성화
            if (guild.isPresident) {
                Button(
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.buttonColors(Color.Red)
                ) {
                    // 회원 추방
                    Text(text = "추방하기", color = Color.White)
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                text = { Text(text = "${member.userNickname}님을 추방할까요?") },
                confirmButton = {
                    Button(
                        onClick = {
                            guildViewModel.exileMember(guild.guildId, member.userId)
                            showDialog = false
                        }
                    ) {
                        Text("추방")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("취소")
                    }
                }
            )
        }
    }
}