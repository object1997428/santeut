package com.santeut.ui.guild

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraX
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.deepl.api.Translator
import com.santeut.data.apiservice.PlantIdApi
import com.santeut.data.model.request.PlantIdentificationRequest
import com.santeut.data.model.response.GuildResponse
import com.santeut.data.util.CameraXFactory
import com.santeut.data.util.CameraXImpl
import com.santeut.data.util.RecordingInfo
import com.ujizin.camposer.CameraPreview
import com.ujizin.camposer.state.ImageCaptureResult
import com.ujizin.camposer.state.rememberCameraState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import retrofit2.http.POST
import retrofit2.Response

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

@Composable
fun TakeCamera(modifier: Modifier = Modifier, onPictureTaken: (File) -> Unit) {
    val cameraState = rememberCameraState()
    val context = LocalContext.current
    val imageFile = remember { mutableStateOf<File?>(null) }
    val fileName = "image_${System.currentTimeMillis()}.jpg"
    val directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//    var camSelector by remember {
//        mutableStateOf(CamSelector.Back)
//    }
    Log.d("진입", "확인")
    CameraPreview(
//        modifier = modifier,
        cameraState = cameraState,
//        camSelector = camSelector
    ){
        Button(onClick = {
            val file = File(directory, fileName)
            cameraState.takePicture(file) { result ->
                if (result is ImageCaptureResult.Success){
                    imageFile.value = file
                    onPictureTaken(file)
                    Log.d("imageFile Value", "File path: ${imageFile.value?.absolutePath}")
                    Toast.makeText(context, "Success!", Toast.LENGTH_LONG).show()
                }
                else {
                    Toast.makeText(context, "Falied", Toast.LENGTH_SHORT).show()
                }
                // Result는 사진이 성공적으로 저장되었는지 여부를 알려줌
                Log.d("카메라", "버튼 클릭")
            }
        }) {  Text("Take Picture") }
//        Button(onClick = {
//            camSelector = camSelector.inverse
//        }) { Text("Switch camera") }

    }
    val capturedImageFile = imageFile.value

    Log.d("찍은 사진", capturedImageFile.toString())

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
//        modifier = modifier,
        cameraState = cameraState,
//        camSelector = camSelector
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
                                .baseUrl("https://plant.id/api/") // BASE_URL은 서버의 기본 URL입니다.
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
                            val text = "Ficus microcarpa, also known as Chinese banyan, Malayan banyan, Indian laurel, curtain fig, or gajumaru (\\u30ac\\u30b8\\u30e5\\u30de\\u30eb), is a tree in the fig family Moraceae. It is native in a range from China through tropical Asia and the Caroline Islands to Australia. It is widely planted as a shade tree and frequently misidentified as F. retusa or as F. nitida (syn. F. benjamina)."
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
        }) {  Text("Take Picture") }
//        Button(onClick = {
//            camSelector = camSelector.inverse
//        }) { Text("Switch camera") }

    }
    val capturedImageFile = imageFile.value

    Log.d("찍은 사진", capturedImageFile.toString())
}

fun encodeFileToBase64Binary(filePath: String): String {
    val file = File(filePath)
    val fileInputStreamReader = FileInputStream(file)
    val bytes = fileInputStreamReader.readBytes()
    fileInputStreamReader.close()
    return "data:image/jpeg;base64," + Base64.encodeToString(bytes, Base64.DEFAULT)
}
fun convertImageFileToBase64(imageFile: File) : String{
    val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    val byteArray = outputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

@Composable
fun CameraUI2(modifier: Modifier = Modifier, onPictureTaken: (String) -> Unit) {
    val cameraState = rememberCameraState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

//    val imageFile = remember { mutableStateOf<File?>(null) }
//    val fileName = "image_${System.currentTimeMillis()}.jpg"
//    val directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//    var camSelector by remember {
//        mutableStateOf(CamSelector.Back)
//    }
    val imageCapture = ImageCapture.Builder().build()
    Log.d("진입", "확인")
    CameraPreview(
        modifier = modifier,
        cameraState = cameraState,
//        camSelector = camSelector
    ){
        Button(onClick = {

            val executor = ContextCompat.getMainExecutor(context)
            cameraProviderFuture.get().unbindAll()

            imageCapture.takePicture(
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        val buffer = image.planes.first().buffer
                        val bytes = ByteArray(buffer.remaining())
                        buffer.get(bytes)
                        image.close()

                        val base64String = Base64.encodeToString(bytes, Base64.DEFAULT)
                        onPictureTaken(base64String)
                        Toast.makeText(context, "Image captured!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.d("Capture failed : ${exception.message}", "Test")
                        System.out.println(exception.message)
                        Toast.makeText(context, "Capture failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }) {
            Text("Take Picture")
        }
//        Button(onClick = {
//            val file = File(directory, fileName)
//            cameraState.takePicture(file) { result ->
//                if (result is ImageCaptureResult.Success){
//                    imageFile.value = file
//                    onPictureTaken(file)
//                    System.out.println(imageFile.value)
//                    Log.d("imageFile Value", "File path: ${imageFile.value}")
//                    Log.d("imageFile Name", "File name: ${imageFile.value?.name}")
//                    Toast.makeText(context, "Success!", Toast.LENGTH_LONG).show()
////                    encodeFileToBase64Binary(imageFile.value.absolutePath)
//                }
//                else {
//                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
//                }
//                // Result는 사진이 성공적으로 저장되었는지 여부를 알려줌
//                Log.d("카메라", "버튼 클릭")
//            }
//        }) {  Text("Take Picture") }

    }
//    val capturedImageFile = imageFile.value
//
//    Log.d("찍은 사진", capturedImageFile.toString())
}
//fun createImageFile(context: Context): File {
//    return File(directory, fileName)
//}

//@Composable
//fun CaptureImage() {
//    var imageFile by remember { mutableStateOf<File?>(null) }
//    var base64Image by remember { mutableStateOf<String?>(null) }
//
//    CameraUI { file ->
//        imageFile = file
//        file?.let{
//            base64Image = convertImageFileToBase64(it)
//            Log.d("Base64 Image", base64Image ?: "Conversion failed")
//        }
//    }
//
//    imageFile?.let { file ->
//        // 이미지 파일이 존재할 때 UI에 보여주는 부분
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            Image(
//                painter = rememberAsyncImagePainter(file.toUri()),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(300.dp)
//                    .clip(shape = RoundedCornerShape(10.dp))
//                    .border(1.dp, Color.Black, shape = RoundedCornerShape(10.dp))
//            )
//        }
//    }
//}

@Composable
 fun CameraScreen(showSnackBar: (String) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraScope = rememberCoroutineScope()
    val context = LocalContext.current
    val cameraX = remember { CameraXFactory.create() }
    val previewView = remember { mutableStateOf<PreviewView?>(null) }
    val facing = cameraX.getFacingState().collectAsState()

    LaunchedEffect(Unit) {
        cameraX.initialize(context = context)
        previewView.value = cameraX.getPreviewView()
        cameraScope.launch(Dispatchers.Main) {
            cameraX.startCamera(lifecycleOwner = lifecycleOwner)
        }
    }
    DisposableEffect(facing.value) {

        onDispose {
            cameraX.unBindCamera()
        }
    }
    Button(
        modifier = Modifier
            .padding(10.dp),
        onClick = {
            Log.d("Camera Button", "Click Click")
            cameraX.takePicture(showSnackBar)
        }
    ) {
        Text("takePicture")
    }
}

fun genderToString(guild: GuildResponse?):String {
    return when (guild?.guildGender) {
        'F' -> "여"
        'M' -> "남"
        else -> "성별 무관"
    }
}