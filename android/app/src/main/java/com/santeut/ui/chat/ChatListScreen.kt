package com.santeut.ui.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ChatListScreen(
    navController: NavController
) {
    Column {
        Text(text = "채팅 목록 페이지")

        LazyColumn {
            items(10) {
                ChatRoom(navController)
            }
        }
    }
}

@Composable
fun ChatRoom(navController: NavController) {

    val partyId = 1

    Card(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
            .clickable(
                onClick = { navController.navigate("chatRoom/$partyId") }
            )
    ) {
        Text(text = "채팅방")
    }
}