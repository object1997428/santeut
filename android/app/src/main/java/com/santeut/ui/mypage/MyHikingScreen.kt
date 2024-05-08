package com.santeut.ui.mypage

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.santeut.ui.community.PartyViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun MyHikingScreen(
    partyViewModel: PartyViewModel = hiltViewModel()
) {
    Text(text = "등산 기록")
}