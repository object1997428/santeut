package com.santeut.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import com.santeut.designsystem.theme.SanteutTheme

@Composable
fun MainScreen (

){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFE5DD90)),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "1:22 PM",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.weight(0.5f))
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .height(60.dp)
                .width(120.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF335C49),
                contentColor = Color.White,

                disabledBackgroundColor = Color.Gray,
                disabledContentColor = Color.DarkGray
            )
        ) {
            Text(
                text = "작시 산등",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.weight(0.6f))
    }
}

@Preview(
    device = Devices.WEAR_OS_LARGE_ROUND,
    showBackground = false,
    showSystemUi = true
)
@Composable
fun MainScreenPreview(){
    SanteutTheme {
        MainScreen(
        )
    }
}