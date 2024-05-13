package com.santeut.ui.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ChatListScreen(
    navController: NavController
) {
    Column {
        LazyColumn {
            items(10) {
                ChatRoom(1, navController)
            }
        }
    }
}

@Composable
fun ChatRoom(partyId: Int, navController: NavController) {

    Box(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
            .clickable(
                onClick = { navController.navigate("chatRoom/$partyId") }
            )
    ) {
        Column() {
            Row () {
                Text(
                    text = "소모임 이름",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "동호회 이름",
                    color = Color.LightGray,
                    modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp)
                )
                Text(
                    text = "8",
                    color = Color.LightGray,
                    modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp)
                )
            }
            Text(
                text = "마지막 대화내용마지막 대화내용마지막 대화내용마지막 대화내용마지막 대화내용마지막 대화내용마지막 대화내용마지막 대화내용마지막 대화내용",
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier.padding(0.dp, 4.dp)
            )
            Text(
                text = "yyyy-MM-dd HH:mm",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Right,
                color = Color.LightGray
            )

        }
    }
}