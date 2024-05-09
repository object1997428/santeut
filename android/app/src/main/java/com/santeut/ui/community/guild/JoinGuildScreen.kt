package com.santeut.ui.community.guild

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.santeut.R
import com.santeut.data.model.response.GuildResponse
import com.santeut.ui.guild.GuildInfoScreen
import com.santeut.ui.guild.GuildViewModel
import com.santeut.ui.guild.genderToString
import com.santeut.ui.guild.regionName

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun JoinGuildScreen(
    navController: NavController,
    guildViewModel: GuildViewModel = hiltViewModel()
) {
    val guilds by guildViewModel.guilds.observeAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        guildViewModel.getGuilds()
    }

    LazyColumn {
        items(guilds) { guild ->
            GuildCard(guild)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun GuildCard(guild: GuildResponse) {

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp,
        onClick = { showBottomSheet = true }
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
                    Text(text = guild.guildName)
                    Text(text = "${guild.guildMember ?: 0}명")
                    Text(text = regionName(guild.regionId))
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            Surface(modifier = Modifier.padding(16.dp)) {
                Column {
                    // 동호회 상세 정보
                    GuildDetail(guild)
                }
            }
        }
    }
}

@Composable
fun GuildDetail(guild: GuildResponse) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {

        AsyncImage(
            model = guild.guildProfile ?: R.drawable.logo,
            contentDescription = "동호회 사진",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)  // 이미지 크기를 지정하여 UI에 맞게 조절
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = guild.guildName,
            style = MaterialTheme.typography.headlineMedium, // 크기와 스타일 조정
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = guild.guildInfo,
            style = MaterialTheme.typography.bodyLarge, // 일관된 텍스트 스타일
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "인원 ${guild.guildMember ?: 0}명",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "성별 ${genderToString(guild)}",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "연령 ${guild.guildMinAge}세 ~ ${guild.guildMaxAge}세",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(onClick = { /*TODO*/ }) { Text(text = "가입 요청하기") }
    }
}