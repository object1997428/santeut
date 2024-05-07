package com.santeut.ui.community

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CommentScreen() {
    Column {
        Text(text = "댓글 작성자 닉네임")
        Text(text = "내용")
        Text(text = "작성 시각")
    }
}