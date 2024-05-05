package com.santeut.ui.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.santeut.data.model.response.PostResponse
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter

@Composable
fun PostTipsScreen(
    navController: NavController,
    postViewModel: PostViewModel = hiltViewModel(),
) {
    var searchText by remember { mutableStateOf("") }
    // 게시글 작성 버튼

    val posts by postViewModel.posts.observeAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("검색") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { navController.navigate("createPost") }) {
                Text("글쓰기")
            }
        }
        // 게시글 목록
        LazyColumn(
            modifier = Modifier
                .background(color = Color.White)
                .fillMaxWidth(),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(posts) { post ->
                TipDetail(post)
            }
        }
    }
}

@Composable
fun TipDetail(post: PostResponse) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(text = post.postTitle, style = MaterialTheme.typography.h6)
        Text(text = post.postContent, style = MaterialTheme.typography.body1)
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Comment,
                    contentDescription = "Comment",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(text = "${post.commentCnt}", style = MaterialTheme.typography.body2)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Favorite",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(text = "${post.likeCnt}", style = MaterialTheme.typography.body2)
            }
            Text(text = formatTime(post.createdAt), style = MaterialTheme.typography.caption)
            Text(text = post.userNickname, style = MaterialTheme.typography.caption)
        }
    }
}

fun formatTime(createdAt: LocalDateTime): String {

    // 현재 시각과의 차이
    val duration = Duration.between(createdAt, LocalDateTime.now()).abs()
    val period = Period.between(createdAt.toLocalDate(), LocalDate.now()).normalized()

    // 하루가 지나면 날짜로 표시
    val oneMinute = Duration.ofMinutes(1)
    val oneHour = Duration.ofHours(1)

    if (duration.compareTo(oneMinute) < 0) {
        return "방금 전"
    } else if (duration.compareTo(oneHour) < 0) {
        val minutes = duration.toMinutes()
        return "$minutes 분 전"
    } else if (period.days < 1) {
        val hours = duration.toHours()
        return "$hours 시간 전"
    } else {
        val formatter = DateTimeFormatter.ofPattern("MM월 dd일")
        return createdAt.format(formatter)
    }
}
