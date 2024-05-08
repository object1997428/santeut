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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Text

@Composable
fun HealthScreen(
) {
    HealthDataScreen(
    )
}

@Composable
fun HealthDataScreen(
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
                    value = "112",
                    unit = "bpm"
                )
            }
            item {
                HealthItem(
                    title = "심박수2",
                    value = "112",
                    unit = "bpm"
                )
            }
            item {
                HealthItem(
                    title = "걸음 수",
                    value = "5,334",
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