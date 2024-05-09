package com.santeut.ui.mountain

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MountainScreen(
    mountainViewModel: MountainViewModel = hiltViewModel()
) {
    Text(text = "산 상세 정보 페이지")
}