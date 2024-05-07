package com.santeut.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.health.services.client.data.DataTypeAvailability
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.santeut.PERMISSION
import com.santeut.data.HealthServicesRepository

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HealthScreen(
    healthServicesRepository: HealthServicesRepository,
    viewModel: HealthViewModel,
    permissionState: PermissionState
) {

    val enabled by viewModel.enabled.collectAsState()
    val hr by viewModel.hr
    val availability by viewModel.availability
    val uiState by viewModel.uiState

    LaunchedEffect(key1 = Unit) {
        if (permissionState.status.isGranted) {
            Log.d("Permission OK", "권한 있음")
        } else {
            Log.d("Permission NO", "권한 없음")
            permissionState.launchPermissionRequest()
        }
    }

    val heartRate = if (availability == DataTypeAvailability.AVAILABLE) {
        String.format("%.4f", hr)
    } else {
        "--"
    }

    HealthDataScreen(
        heartRate = heartRate
    )
}

@Composable
fun HealthDataScreen(
    heartRate: String
){
    val listState = rememberScalingLazyListState()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF335C49)),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "건강 정보",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        ScalingLazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp, 0.dp, 20.dp, 0.dp),
            state = listState,
            autoCentering = AutoCenteringParams(0)
        ){
            item {
                HealthItem(
                    title = "심박수",
                    value = heartRate,
                    unit = "bpm"
                )
            }
            item {
                HealthItem(
                    title = "칼로리",
                    value = "252",
                    unit = "kcal"
                )
            }
            item {
                HealthItem(
                    title = "걸음 수",
                    value = "5,334",
                    unit = ""
                )
            }
            item {
                HealthItem(
                    title = "이동거리",
                    value = "1,000",
                    unit = "m"
                )
            }
            item {
                HealthItem(
                    title = "고도",
                    value = "507",
                    unit = "m"
                )
            }
            item {
                HealthItem(
                    title = "등산 시간",
                    value = "01:37:19",
                    unit = ""
                )
            }
        }
    }
}

@Composable
fun HealthItem(
    title: String,
    value: String,
    unit: String
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .border(0.dp, color = Color(0xFF335C49), shape = RoundedCornerShape(20.dp))
            .clip(shape = RoundedCornerShape(20.dp)),
    ){
        Row (
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFE5DD90)),
            verticalAlignment = Alignment.CenterVertically
        ){
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.Black,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = value,
                fontSize = 14.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = unit,
                fontSize = 10.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
    }
}