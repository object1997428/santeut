package com.santeut.ui.party

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.santeut.ui.party.PartyViewModel

@Composable
fun PartyListScreen(
    partyViewModel: PartyViewModel = hiltViewModel()
) {
    Text(text = "내 소모임 목록")
}
