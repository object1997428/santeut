package com.santeut.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import com.santeut.ui.SummaryState.MainViewModel

@Composable
fun MainScreen(
) {
    val viewModel = hiltViewModel<MainViewModel>()

    val state by viewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFE5DD90)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.5f))
        if(!state){
            Button(
                onClick = {
                    viewModel.startExercise()
                },
                modifier = Modifier
                    .height(60.dp)
                    .width(120.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF335C49),
                    contentColor = Color.White,
                )
            ) {
                Text(
                    text = "측정 시작",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        else{
            Button(
                onClick = {
                    viewModel.endExercise()
                },
                modifier = Modifier
                    .height(60.dp)
                    .width(120.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Gray,
                    contentColor = Color.LightGray,
                )
            ) {
                Text(
                    text = "측정 종료",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.weight(0.5f))
    }
}