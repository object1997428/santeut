package com.santeut.ui.map

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santeut.data.apiservice.PlantIdApi
import com.santeut.data.model.request.PlantIdentificationRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.jsoup.Jsoup
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject

@HiltViewModel
class PlantViewModel @Inject constructor()
    : ViewModel() {

    val plantName = mutableStateOf("")
    val plantDescription = mutableStateOf("")
    val crawlPlantName = mutableStateOf("")
    val crawlPlantDescription = mutableStateOf("")
    val name = mutableStateOf("")
    val description = mutableStateOf("")
    val isLoading = mutableStateOf(true)

//    val authKey = "897064a0-60a9-45ac-a0b8-2bc9e8ec0837:fx"
//    val translator = Translator(authKey)

    fun identifyPlant(file: File) {

        viewModelScope.launch(Dispatchers.IO) {
            val filePath = file.absolutePath
            val base64Image = encodeFileToBase64Binary(filePath)

            val retrofit = Retrofit.Builder()
                .baseUrl("https://plant.id/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val plantIdApi = retrofit.create(PlantIdApi::class.java)

            val request = PlantIdentificationRequest(images = base64Image, similar_images = true)
            val response = plantIdApi.identifyPlant(request)

            if (response.isSuccessful) {
                val jsonResponse = response.body()?.string()
                if (jsonResponse != null) {
                    val jsonObject = JSONObject(jsonResponse)
                    val resultObject = jsonObject.getJSONObject("result")
                    val classificationObject = resultObject.getJSONObject("classification")
                    val suggestionsArray = classificationObject.getJSONArray("suggestions")

                    if (suggestionsArray.length() > 0) {
                        val suggestionObject = suggestionsArray.getJSONObject(0)
                        plantName.value = suggestionObject.getString("name")
                        val detailsObject = suggestionObject.getJSONObject("details")
                        val descriptionObject =
                            detailsObject.getJSONObject("description")
                        plantDescription.value = descriptionObject.getString("value")

                        Log.d("Plant Name", "${plantName.value}")

                        val url = "https://terms.naver.com/search.naver?query=${plantName.value}&searchType=text&dicType=&subject="
                        val docs = Jsoup.connect(url).get()

                        val h4Element = docs.selectFirst("h4")

                        // h4 요소가 null이 아닌지 확인
                        if (h4Element != null) {
                            // keyword와 desc 클래스를 포함한 텍스트를 추출
                            val keywordText = h4Element.selectFirst(".keyword a")?.text()
                            val descText = h4Element.selectFirst(".desc")?.text()

                            // 두 텍스트를 결합하여 제목을 설정
                            val titleText = "$keywordText $descText"

                            // 꽃 이름 출력
                            Log.d("crawlPlant Name", "${keywordText}")
                            crawlPlantName.value = keywordText.toString()

//                                        val description = docs.selectFirst("p.desc")?.text()
                            val description = docs.selectFirst("p.desc.__ellipsis")?.text()
//                                        val description = descElement?.text()

                            if (description != null) {
                                Log.d("Plant Description", description)
                                crawlPlantDescription.value = description
                                // 가져온 텍스트를 변수에 저장하거나 다른 작업을 수행할 수 있습니다.
                            } else {
                                Log.d("Description", "Description not found")
                            }

                            if (keywordText != null && description != null) isLoading.value = false
                        } else {
                            Log.d("Search Error", "정보를 찾을 수 없습니다.")
                        }
                    }
                }
            } else {
                Log.e("", "식물 정보 불러오기 실패")
            }
        }
    }

    private fun encodeFileToBase64Binary(filePath: String): String {
        val file = File(filePath)
        val fileInputStreamReader = FileInputStream(file)
        val bytes = fileInputStreamReader.readBytes()
        fileInputStreamReader.close()
        return "data:image/jpeg;base64," + Base64.encodeToString(bytes, Base64.DEFAULT)
    }
}