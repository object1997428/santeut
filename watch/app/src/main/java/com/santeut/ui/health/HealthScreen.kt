package com.santeut.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.santeut.ui.health.HealthScreenState

@Composable
fun HealthScreen(
    state: Boolean,
    uiState: HealthScreenState
) {


    if (uiState.error == null){
        if(!state){
            PrepareScreen()
        }
        else {
            HealthDataScreen(
                uiState = uiState
            )
        }
    }else{
        NotSupportedScreen()
    }
}

@Composable
fun HealthDataScreen(
    uiState: HealthScreenState,
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
            fontSize = 14.sp,
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
                    value = uiState.exerciseState?.exerciseMetrics?.heartRate?.toInt()?.toString() ?: "--",
                    unit = "bpm"
                )
            }
            item {
                HealthItem(
                    title = "이동거리",
                    value = "%.2f".format(uiState.exerciseState?.exerciseMetrics?.distance?.div(1000) ?: 0.0).takeIf { it != "0.00" } ?: "--",
                    unit = "km"
                )
            }
            item {
                HealthItem(
                    title = "걸음 수",
                    value = uiState.exerciseState?.exerciseMetrics?.stepsTotal?.toString() ?: "--",
                    unit = "걸음"
                )
            }
            item {
                HealthItem(
                    title = "칼로리",
                    value = uiState.exerciseState?.exerciseMetrics?.calories?.toInt()?.toString() ?: "--",
                    unit = "kcal"
                )
            }
            item {
                HealthItem(
                    title = "오른 높이",
                    value = uiState.exerciseState?.exerciseMetrics?.elevationGainTotal?.toInt()?.toString() ?: "--",
                    unit = "m"
                )
            }
            item {
                HealthItem(
                    title = "고도",
                    value = uiState.exerciseState?.exerciseMetrics?.absoluteElevation?.toInt()?.toString() ?: "--",
                    unit = "m"
                )
            }
            item {
                HealthItem(
                    title = "위도",
                    value = uiState.exerciseState?.exerciseMetrics?.location?.latitude?.let {
                        String.format("%.6f", it)
                    } ?: "--",
                    unit = ""
                )
            }
            item {
                HealthItem(
                    title = "경도",
                    value = uiState.exerciseState?.exerciseMetrics?.location?.longitude?.let {
                        String.format("%.6f", it)
                    } ?: "--",
                    unit = ""
                )
            }
            item {
                HealthItem(
                    title = "고도",
                    value = uiState.exerciseState?.exerciseMetrics?.location?.altitude?.toLong()?.toString() ?: "--",
                    unit = ""
                )
            }
            item {
                HealthItem(
                    title = "방향",
                    value = uiState.exerciseState?.exerciseMetrics?.location?.bearing?.toInt()?.toString() ?: "--",
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

@Composable
fun PrepareScreen(){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF335C49)),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "건강 정보",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(0.4f))
        Text(text = "등산을")
        Text(text = "시작해 주세요.")
        Spacer(modifier = Modifier.weight(0.6f))
    }
}

@Composable
fun NotSupportedScreen(){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF335C49)),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "건강 정보",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(0.4f))
        Text(text = "지원하지 않는")
        Text(text = "모델입니다.")
        Spacer(modifier = Modifier.weight(0.6f))
    }
}

@Composable
fun formatDistanceKm(meters: Double?) = buildAnnotatedString {
    if (meters == null) {
        append("--")
    } else {
        append("%02.2f".format(meters / 1_000))
        withStyle(style = MaterialTheme.typography.caption3.toSpanStyle()) {
            append("km")
        }
    }
}