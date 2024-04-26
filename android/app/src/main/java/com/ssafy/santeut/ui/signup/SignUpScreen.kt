package com.ssafy.santeut.ui.signup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SignUpScreen(
    onNavigateLogin: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Text(text = "회원가입 페이지")
    }
}