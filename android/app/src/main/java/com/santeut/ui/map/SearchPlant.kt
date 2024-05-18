package com.santeut.ui.map

import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.deepl.api.Translator
import com.santeut.data.apiservice.PlantIdApi
import com.santeut.data.model.request.PlantIdentificationRequest
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


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchPlant(hikingViewModel: HikingViewModel = hiltViewModel()) {

    val cameraState = rememberCameraState()
    val context = LocalContext.current
    val imageFile = remember { mutableStateOf<File?>(null) }
    val fileName = "image_${System.currentTimeMillis()}.jpg"
    val directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
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
                        text = hikingViewModel.name.value,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = hikingViewModel.description.value,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    ) {
        CameraPreview(
            cameraState = cameraState,
        ) {
            Button(onClick = {

                coroutineScope.launch {
                    if (sheetState.isVisible) {
                        sheetState.hide()
                    } else {
                        sheetState.show()
                    }
                }

                val file = File(directory, fileName)
                cameraState.takePicture(file) { result ->
                    if (result is ImageCaptureResult.Success) {
                        imageFile.value = file

                        CoroutineScope(Dispatchers.IO).launch {
                            hikingViewModel.identifyPlant(file)
                            sheetState.show()
                        }
                    } else {
                        Toast.makeText(context, "사진 촬영에 실패했습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "사진 촬영")
            }
        }
    }
}
