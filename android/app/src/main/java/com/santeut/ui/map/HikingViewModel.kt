package com.santeut.ui.map

import android.util.Base64
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deepl.api.Translator
import com.google.firebase.database.tubesock.WebSocketMessage
import com.google.gson.Gson
import com.santeut.MainApplication
import com.santeut.data.apiservice.PlantIdApi
import com.santeut.data.model.request.EndHikingRequest
import com.santeut.data.model.request.PlantIdentificationRequest
import com.santeut.data.model.request.StartHikingRequest
import com.santeut.data.model.response.CourseDetailResponse
import com.santeut.data.model.response.LocationDataResponse
import com.santeut.data.model.response.WebSocketMessageResponse
import com.santeut.domain.usecase.HikingUseCase
import com.santeut.domain.usecase.PartyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import org.jsoup.Jsoup
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject

@HiltViewModel
class HikingViewModel @Inject constructor(
    private val partyUseCase: PartyUseCase,
    private val hikingUseCase: HikingUseCase
) : ViewModel() {

    var webSocket: WebSocket? = null

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _distance = MutableLiveData<Double>()
    val distance: LiveData<Double> = _distance

    private val _courseList = MutableLiveData<List<LocationDataResponse>>()
    val courseList: LiveData<List<LocationDataResponse>> = _courseList

    fun startHiking(startHikingRequest: StartHikingRequest) {
        viewModelScope.launch {
            try {
                _distance.postValue(hikingUseCase.startHiking(startHikingRequest).distance)
                _courseList.postValue(hikingUseCase.startHiking(startHikingRequest).courseList)
            } catch (e: Exception) {
                _error.postValue("소모임 시작 실패: ${e.message}")
            }
        }
    }

    fun endHiking(endHikingRequest: EndHikingRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                hikingUseCase.endHiking(endHikingRequest).collect {
                    Log.d("HikingViewModel", "소모임 종료 성공")
                }
            } catch (e: Exception) {
                _error.postValue("소모임 종료 실패: ${e.message}")
            }
        }
    }
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


    fun startWebSocket(
        partyId: Int,
        updateLocation: (String, Double, Double, String?) -> Unit,
        showAlert: MutableState<Boolean>,
        alertMessage: MutableState<String>
    ) {
        // Web Socket
        val webSocketUrl = "wss://k10e201.p.ssafy.io/api/hiking/chat/rooms/${partyId}"

        val request = Request.Builder()
            .header(
                "Authorization",
                "Bearer ${MainApplication.sharedPreferencesUtil.getAccessToken()}"
            )
            .url(webSocketUrl)
            .build()

        val listener = object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "Received message: $text")
                try {
                    val message = Gson().fromJson(text, WebSocketMessageResponse::class.java)
                    updateLocation(
                        message.userNickname,
                        message.lat.toDouble(),
                        message.lng.toDouble(),
                        message.userProfile
                    )

                    if (message.type == "offCourse") {
                        showAlert.value = true
                        alertMessage.value = "${message.userNickname}님이 경로를 이탈하셨습니다."
                    }
                } catch (e: Exception) {
                    Log.e("WebSocket", "Error parsing message: ${e.localizedMessage}", e)
                }
            }

            override fun onOpen(webSocket: WebSocket, response: Response) {
                webSocket.send("{\"type\": \"enter\"}")
                Log.d("WebSocket", "WebSocket opened and initial message sent.")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WebSocket", "WebSocket is closing: $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "WebSocket connection failure: ${t.localizedMessage}", t)
            }
        }

        webSocket = OkHttpClient().newWebSocket(request, listener)
    }

    fun stopWebSocket() {
        webSocket?.send("{\"type\": \"quit\"}")
        webSocket?.close(1000, "Activity Ended")
        webSocket = null
    }
}