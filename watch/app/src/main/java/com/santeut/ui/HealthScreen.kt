package com.santeut.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Text
import com.santeut.designsystem.theme.SanteutTheme

@Composable
fun HealthScreen(
) {
    val listState = rememberScalingLazyListState()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF335C49)),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "1:22 PM",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(2.dp))
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
                    value = "111",
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

@Preview(
    device = Devices.WEAR_OS_LARGE_ROUND,
    showBackground = false,
    showSystemUi = true
)
@Composable
fun HealthScreenPreview(){
    SanteutTheme {
        HealthScreen()
    }
}
