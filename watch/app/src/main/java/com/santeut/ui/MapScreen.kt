package com.santeut.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.santeut.designsystem.theme.SanteutTheme

@Composable
fun MapScreen(
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFDDE5D5)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "지도요",
                color = Color.Black
            )
        }
        Text(
            text = "1:22 PM",
            color = Color.Black,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(
    device = Devices.WEAR_OS_LARGE_ROUND,
    showBackground = false,
    showSystemUi = true
)
@Composable
fun MapScreenPreview(){
    SanteutTheme {
        MapScreen(
        )
    }
}