package com.santeut.ui.guild

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.santeut.data.model.response.GuildPostResponse
import com.santeut.ui.community.tips.formatTime

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GuildCommunityScreen(
    guildId: Int,
    navController: NavController,
    guildViewModel: GuildViewModel = hiltViewModel()
) {

    val postList by guildViewModel.postList.observeAsState(emptyList())

    LaunchedEffect(key1 = null) {
        guildViewModel.getGuildPostList(guildId, 0)
        guildViewModel.getGuildPostList(guildId, 1)
    }

    Scaffold(
        floatingActionButton = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                FloatingActionButton(onClick = { navController.navigate("createGuildPost/$guildId") }) {
                    Row(modifier = Modifier) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = "글쓰기")
                        Text(text = "글쓰기")
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) {
        if (postList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                androidx.compose.material3.Text(
                    text = "게시글이 없습니다.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            LazyColumn() {
                items(postList) { post ->
                    GuildPost(post, navController)
                }
            }
        }
    }
}

@Composable
fun GuildPost(post: GuildPostResponse, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { navController.navigate("getGuildPost/${post.guildPostId}") })
    ) {
        Text(text = post.guildPostTitle)
        Text(text = post.guildPostContent)
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
                Text(
                    text = "${post.commentCnt}",
                    style = MaterialTheme.typography.body2
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Favorite",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "${post.likeCnt}",
                    style = MaterialTheme.typography.body2
                )
            }
            Text(
                text = formatTime(post.createdAt),
                style = MaterialTheme.typography.caption
            )
            Text(
                text = post.userNickName,
                style = MaterialTheme.typography.caption
            )
        }
    }
}
