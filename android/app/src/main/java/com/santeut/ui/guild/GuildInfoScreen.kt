package com.santeut.ui.guild

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.santeut.R
import com.santeut.data.model.response.GuildResponse

@Composable
fun GuildInfoScreen(guild: GuildResponse?) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {

        if (guild != null) {
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
        }
    }
}

fun genderToString(guild: GuildResponse?):String {
    return when (guild?.guildGender) {
        'F' -> "여"
        'M' -> "남"
        else -> "성별 무관"
    }
}