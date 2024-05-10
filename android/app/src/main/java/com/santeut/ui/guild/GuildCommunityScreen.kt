package com.santeut.ui.guild

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun GuildCommunityScreen(
    guildViewModel: GuildViewModel = hiltViewModel()
) {
    Text(text = "Guild Community Screen")
}