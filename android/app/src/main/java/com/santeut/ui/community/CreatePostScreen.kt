package com.santeut.ui.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CreatePostScreen(
    navController: NavController
) {
    WritePost()
    Text(text = "글작성 페이지")
}

@Composable
fun WritePost(
) {
    Column(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            Alignment.Center
        ) { Text(text = "글쓰기", style = MaterialTheme.typography.body2) }
    }

}

@Composable
@Preview
fun Preview() {
    WritePost()
}