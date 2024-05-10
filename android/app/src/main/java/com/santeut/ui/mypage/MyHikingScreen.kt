package com.santeut.ui.mypage

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.santeut.ui.party.PartyViewModel

@Composable
fun MyHikingScreen(
    partyViewModel: PartyViewModel = hiltViewModel()
) {
    Text(text = "등산 기록")
}