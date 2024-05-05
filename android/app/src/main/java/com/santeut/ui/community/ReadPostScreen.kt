package com.santeut.ui.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ReadPostScreen() {
    val focusManager = LocalFocusManager.current
    var comment by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            HeaderSection()
            ContentSection()
            CommentSection(
                comment,
                onCommentChange = { comment = it },
                onSend = { focusManager.clearFocus() })
        }
    }
}

@Composable
fun HeaderSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "카테고리", style = MaterialTheme.typography.bodyMedium)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(text = "제목", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = "Like",
                modifier = Modifier.size(24.dp)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(text = "닉네임", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "작성 시간", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ContentSection() {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(200.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Text(text = "내용", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun CommentSection(
    comment: String,
    onCommentChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        LazyColumn(modifier = Modifier
            .weight(1f)
            .padding(bottom = 56.dp)) {
            items(30) { PostCommentScreen() }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .background(Color.White)
        ) {
            TextField(
                value = comment,
                onValueChange = onCommentChange,
                placeholder = { Text("내용") },
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onSend() })
            )
            IconButton(onClick = onSend) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "Send"
                )
            }
        }
    }
}

@Composable
@Preview
fun PreviewReadPostScreen() {
    ReadPostScreen()
}
