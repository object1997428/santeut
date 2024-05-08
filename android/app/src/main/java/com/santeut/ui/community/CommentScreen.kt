package com.santeut.ui.community

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CommentScreen(
    postId: Int, postType: Char,
    commonViewModel: CommonViewModel
) {

    val comments by commonViewModel.comments.observeAsState(initial = emptyList())

    LaunchedEffect(key1 = postId, key2 = postType) {
        commonViewModel.getComment(postId, postType)
    }

    LazyColumn(
        modifier = Modifier
            .padding(bottom = 56.dp)
    ) {
        items(comments) { comment ->
            Column {
                Text(text = comment.userNickname)
                Text(text = comment.commentContent)
                Text(text = formatTime(comment.createdAt))
            }
        }
    }


}