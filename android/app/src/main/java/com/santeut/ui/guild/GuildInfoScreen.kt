package com.santeut.ui.guild

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import coil.compose.AsyncImage
import com.santeut.R
import com.santeut.data.model.response.GuildResponse

@Composable
fun GuildInfoScreen(guild: GuildResponse) {
    Column {

        val gender = when (guild.guildGender) {
            'F' -> "여"
            'M' -> "남"
            else -> "성별 무관"
        }

        AsyncImage(
            model = guild.guildProfile ?: R.drawable.logo,
            contentDescription = "동호회 사진"
        )

        Text(text = guild.guildName)
        Text(text = guild.guildInfo)

        Row {
            Text(text = "인원 " + guild.guildMember.toString() + "명")
            Text(text = "성별 $gender")
            Text(text = "연령 " + guild.guildMinAge + "세 ~ " + guild.guildMaxAge + "세")
        }
    }
}