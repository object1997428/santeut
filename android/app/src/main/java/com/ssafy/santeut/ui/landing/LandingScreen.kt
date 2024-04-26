package com.ssafy.santeut.ui.landing

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LandingScreen(
    onNavigateLogin: () -> Unit,
    onNavigateHome: () -> Unit,
    viewModel: UserViewModel = viewModel()
) {

    val state = viewModel.state

    viewModel.checkAuth()

    LaunchedEffect (state.token){
        Log.d("Landing... ", state.token)

        if(state.token.isBlank()){
            // 토큰 검증이 아니라 자동 로그인이 더 알맞을 수도 있을 것 같음.
            onNavigateLogin()
        }else {
            onNavigateHome()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Text(text = "로딩 중...")
    }
}