package com.santeut.ui.guild

import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.deepl.api.Translator
import com.santeut.data.apiservice.PlantIdApi
import com.santeut.data.model.request.PlantIdentificationRequest
import com.santeut.data.model.response.GuildResponse
import com.ujizin.camposer.CameraPreview
import com.ujizin.camposer.state.ImageCaptureResult
import com.ujizin.camposer.state.rememberCameraState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileInputStream

@Composable
fun GuildInfoScreen(guild: GuildResponse?) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {

        if (guild != null) {
            AsyncImage(
                model = guild.guildProfile,
                contentDescription = "동호회 사진",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)  // 이미지 크기를 지정하여 UI에 맞게 조절
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = guild.guildName?:"",
                style = MaterialTheme.typography.headlineMedium, // 크기와 스타일 조정
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = guild.guildInfo?:"",
                style = MaterialTheme.typography.bodyLarge, // 일관된 텍스트 스타일
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "인원 ${guild.guildMember}명",
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

fun regionName(regionId: Int?): String {
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
        else -> "지역 무관"
    }
}
