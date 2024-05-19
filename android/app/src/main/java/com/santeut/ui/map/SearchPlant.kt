package com.santeut.ui.map

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ujizin.camposer.CameraPreview
import com.ujizin.camposer.state.ImageCaptureResult
import com.ujizin.camposer.state.rememberCameraState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchPlant(plantViewModel: PlantViewModel = hiltViewModel()) {

    val cameraState = rememberCameraState()
    val context = LocalContext.current
    val imageFile = remember { mutableStateOf<File?>(null) }
    val fileName = "image_${System.currentTimeMillis()}.jpg"
    val directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val isButton = remember { mutableStateOf<Boolean>(false) }

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    fun loadAndRotateImage(filePath: String, rotationDegrees: Float): ImageBitmap? {
        val bitmap = BitmapFactory.decodeFile(filePath) ?: return null
        val matrix = Matrix().apply { postRotate(rotationDegrees) }
        val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        return rotatedBitmap.asImageBitmap()
    }
 
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
                        plantViewModel.identifyPlant(file)
                        isButton.value = true;
//                                sheetState.show()
                    }
                } else {
                    Toast.makeText(context, "사진 촬영에 실패했습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
        ) {
            Icon(Icons.Rounded.Add, contentDescription = "사진 촬영")
        }
    }

    if(plantViewModel.isLoading.value && isButton.value) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f)),
            contentAlignment = Alignment.Center
        )
        {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "식물 탐색 중 입니다.",
                    color = Color.Green,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

    }

    if (!plantViewModel.isLoading.value) {

        imageFile.value?.let { file ->
            loadAndRotateImage(file.absolutePath, 90f)?.let { imageBitmap ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(Color.White.copy(alpha = 0.7f))
                )
                Image(
                    bitmap = imageBitmap,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
//                        .height(400.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetContent = {

                Column(
                    modifier = Modifier
//                            .align(Alignment.CenterHorizontally) // 하단 중앙 정렬
                        .padding(16.dp)
//                                .background(Color.Black.copy(alpha = 0.7f))
                ) {
                    Text(
                        text = plantViewModel.crawlPlantName.value,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = plantViewModel.crawlPlantDescription.value,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .fillMaxHeight()
//                        .background(Color.White.copy(alpha = 0.7f))
//                        .padding(16.dp)
//                )

                    }
//                }

        ){}

    }
}
