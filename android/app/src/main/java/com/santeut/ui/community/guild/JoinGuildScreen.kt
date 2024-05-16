package com.santeut.ui.community.guild

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.santeut.R
import com.santeut.data.model.response.GuildResponse
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
    Column {
        // 검색 필드

        var name by remember { mutableStateOf("") }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
//                .height(80.dp),
//                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
//                    .padding(start = 25.dp, top = 25.dp, bottom = 25.dp),
                    .padding(top = 8.dp, bottom = 8.dp),
                textStyle = TextStyle(fontSize = 12.sp, color = Color(0xff666E7A)),
                value = name,
                onValueChange = { name = it },
                placeholder = {
                    Text(
                        text = "어느 동호회를 찾으시나요?",
                        color = Color(0xff666E7A)
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done // 완료 액션 지정
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFD6D8DB),
                    unfocusedContainerColor = Color(0xFFEFEFF0),
                    focusedBorderColor = Color(0xFFD6D8DB),
                    focusedContainerColor = Color(0xFFEFEFF0),
                ),

                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "검색",
                        tint = Color(0xff33363F),
                        modifier = Modifier.size(30.dp)
//                        modifier = Modifier.clickable {
//                            val path =
//                                if (region.isNullOrEmpty()) "mountainList/$name" else "mountainList/$name/$region"
//                            navController.navigate(path)
//                        }
                    )
                }
            )
            Spacer(modifier = Modifier.width(15.dp))
            androidx.compose.material3.Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "필터",
                tint = Color(0xff335C49),
                modifier = Modifier.size(30.dp)
//                modifier = Modifier.clickable {
//                    val path =
//                        if (region.isNullOrEmpty()) "mountainList/$name" else "mountainList/$name/$region"
//                    navController.navigate(path)
//                }
            )

        }

        // 동호회 카드 필드
        LazyColumn {
            items(guilds) { guild ->
                GuildCard(guild, guildViewModel)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun GuildCard(guild: GuildResponse, guildViewModel: GuildViewModel) {

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    Card(
        modifier = Modifier
            .clickable(onClick = { showBottomSheet = true })
            .fillMaxWidth()
            .height(100.dp)  // 카드 높이 조정
            .padding(8.dp),  // 카드 주변 여백
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp,

        ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 이미지 부분
            AsyncImage(
                model = guild.guildProfile ?: R.drawable.logo,
                contentDescription = "동호회 사진",
                modifier = Modifier
                    .size(100.dp),  // 이미지 크기 고정
                contentScale = ContentScale.Crop  // 이미지 채우기 방식
            )

            Spacer(modifier = Modifier.width(10.dp))  // 이미지와 텍스트 사이 간격

            // 텍스트 정보 부분
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)  // 텍스트 내부 여백
                    .weight(1f),  // 남은 공간 모두 사용
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                androidx.compose.material3.Text(
                    text = guild.guildName,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 5.dp, top = 5.dp))
                {
                    Icon(
                        imageVector = Icons.Filled.PersonOutline,
                        contentDescription = "회원수",
                        modifier = Modifier.size(20.dp),
                        tint = Color(0xff76797D),
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Adds spacing between icon and text
                    androidx.compose.material3.Text(
                        text = "${guild.guildMember ?: 0} 명",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xff76797D)
                    )
                }
//                --------------------------

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 10.dp)) {
                    Icon(
                        imageVector = Icons.Filled.PinDrop,
                        contentDescription = "지역",
                        modifier = Modifier.size(20.dp),
                        tint = Color(0xff76797D)
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Adds spacing between icon and text
                    androidx.compose.material3.Text(
                        text = regionName(guild.regionId),
                        color = Color(0xff76797D),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
//                --------------------------
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
                    GuildDetail(guild, guildViewModel)
                }
            }
        }
    }
}

@Composable
fun GuildDetail(guild: GuildResponse, guildViewModel: GuildViewModel) {

    var isRequested by remember { mutableStateOf(false) }
    val buttonBackgroundColor =
        if (isRequested) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary
    val buttonText = if (isRequested) "가입 요청 완료" else "가입 요청하기"

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AsyncImage(
            model = guild.guildProfile ?: R.drawable.logo,
            contentDescription = "동호회 사진",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = guild.guildName,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = guild.guildInfo,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
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

        Button(
            onClick = {
                if (!isRequested) {
                    guildViewModel.applyGuild(guild.guildId)
                    isRequested = true
                }
            },
            colors = ButtonDefaults.buttonColors(buttonBackgroundColor)
        ) {
            Text(text = buttonText)
        }
    }
}
