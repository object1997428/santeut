package com.santeut.ui.guild

import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.deepl.api.Translator
import com.santeut.R
import com.santeut.data.apiservice.PlantIdApi
import com.santeut.data.model.request.PlantIdentificationRequest
import com.santeut.data.model.response.GuildResponse
import com.santeut.designsystem.theme.DarkGreen
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
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Top
    ) {

        if (guild != null) {

            AsyncImage(
                model = guild.guildProfile ?: R.drawable.logo,
                contentDescription = "동호회 사진",
                contentScale = ContentScale.Crop,
                modifier = Modifier
//                    .align(Alignment.TopStart)
                    .fillMaxWidth()
//                    .height(300.dp)  // 이미지 크기를 지정하여 UI에 맞게 조절
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxHeight(.3f)
            )

            Column(modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxHeight()
            ) {

                Spacer(modifier = Modifier.height(8.dp))

                // 동호회 이름
                Text(
                    text = guild.guildName ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 동호회 설명
                Text(
                    text = guild.guildInfo ?: "",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 동호회 인원, 성별, 연령
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(
                            text = "인원 ",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Text(
                            text = "${guild.guildMember}명",
                            color = DarkGreen,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(
                            text = "성별 ",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Text(
                            text = "${genderToString(guild)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(
                            text = "연령 ",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Text(
                            text = "${guild.guildMinAge}세 ~ ${guild.guildMaxAge}세",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGreen,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CameraUI() {
    val cameraState = rememberCameraState()
    val context = LocalContext.current
    val imageFile = remember { mutableStateOf<File?>(null) }
    val fileName = "image_${System.currentTimeMillis()}.jpg"
    val directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    Log.d("진입", "확인")
    CameraPreview(
        cameraState = cameraState,
    ){
        Button(onClick = {
            val file = File(directory, fileName)
            cameraState.takePicture(file) { result ->
                if (result is ImageCaptureResult.Success){
                    imageFile.value = file
                    System.out.println(imageFile.value)

                    Log.d("imageFile Value", "File path: ${imageFile.value}")
                    Log.d("imageFile Name", "File name: ${imageFile.value?.name}")
                    Toast.makeText(context, "Success!", Toast.LENGTH_LONG).show()

                    CoroutineScope(Dispatchers.IO).launch {

                        val filePath = imageFile.value?.absolutePath
                        if (filePath != null) {
                            val base64File = encodeFileToBase64Binary(filePath)
                            Log.d("base64File: ", base64File)

                            // API
                            val request = PlantIdentificationRequest(images = base64File, similar_images = true)
                            val retrofit = Retrofit.Builder()
                                .baseUrl("https://plant.id/api/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()
                            val plantIdApi = retrofit.create(PlantIdApi::class.java)

                            try {
                                val response = plantIdApi.identifyPlant(request)
                                if (response.isSuccessful) {
                                    Log.d("API Response", "Success: ${response.body()?.string()}")
                                } else {
                                    Log.d("API Response", "Failure: ${response.errorBody()?.string()}")
                                }
                                val jsonResponse = response.body()?.string()

                                // JSON 파싱
                                val jsonObject = JSONObject(jsonResponse)
                                val resultObject = jsonObject.getJSONObject("result")
                                val classificationObject = resultObject.getJSONObject("classification")
                                val suggestionsArray = classificationObject.getJSONArray("suggestions")

                                // 첫 번째 suggestion의 description 값만 추출하여 로그에 출력
                                if (suggestionsArray.length() > 0) {
                                    val suggestionObject = suggestionsArray.getJSONObject(0)
                                    val detailsObject = suggestionObject.getJSONObject("details")
                                    val descriptionObject = detailsObject.getJSONObject("description")
                                    val descriptionValue = descriptionObject.getString("value")

                                    // description 값 로깅
                                    Log.d("API Response", "Description: ${descriptionValue}")
                                }
                            } catch (e: Exception) {
                                Log.d("API Response", "Error: ${e.message}")
                            }
                            val authKey = "897064a0-60a9-45ac-a0b8-2bc9e8ec0837:fx"
                            val translator = Translator(authKey)
                            val text = "Matricaria chamomilla (synonym: Matricaria recutita), commonly known as chamomile (also spelled camomile), German chamomile, Hungarian chamomile (kamilla), wild chamomile, blue chamomile, or scented mayweed, is an annual plant of the composite family Asteraceae. Commonly, the name M. recutita is applied to the most popular source of the herbal product chamomile, although other species are also used as chamomile. Chamomile is known mostly for its use against gastrointestinal problems; additionally, it can be used to treat irritation of the skin.";
                            val result = translator.translateText(text, null, "ko")
                            Log.d("Translate Result: ", result.text)
                        }
                    }
                }
                else {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                }
                // Result는 사진이 성공적으로 저장되었는지 여부를 알려줌
                Log.d("카메라", "버튼 클릭")
            }
        }
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Take Picture")
        }
    }

    imageFile.value?.let { file ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.7f))
                .padding(16.dp)
        ) {
            Image(
                bitmap = BitmapFactory.decodeFile(file.absolutePath).asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "독우산광대버섯",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "독우산광대버섯은 맹독성 독버섯의 일종으로 전체가 흰색을 띠고 있다. 생김새가 양송이버섯과 흰우산버섯, 흰주름버섯과 비슷하게 생겼다. 일단 이 버섯을 한 조각이라도 먹게 되면 몸속의 여러 부위의 세포와 내부 장기가 파괴되어 심한 복통, 구토, 설사 등의 콜레라 증상을 보이다가 며칠 만에 사망할 수 있다.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


fun encodeFileToBase64Binary(filePath: String): String {
    val file = File(filePath)
    val fileInputStreamReader = FileInputStream(file)
    val bytes = fileInputStreamReader.readBytes()
    fileInputStreamReader.close()
    return "data:image/jpeg;base64," + Base64.encodeToString(bytes, Base64.DEFAULT)
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
