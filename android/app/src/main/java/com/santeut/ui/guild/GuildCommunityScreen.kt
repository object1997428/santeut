package com.santeut.ui.guild

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.santeut.data.model.response.GuildPostResponse
import com.santeut.ui.community.tips.formatTime

@Composable
fun GuildCommunityScreen(
    guildId:Int,
    guildViewModel: GuildViewModel = hiltViewModel()
) {

    val postList by guildViewModel.postList.observeAsState(emptyList())

    LaunchedEffect(key1 = null) {
        guildViewModel.getGuildPostList(guildId, 1)
        guildViewModel.getGuildPostList(guildId, 2)
    }

    if(postList.isEmpty()){
        Text(text = "게시글이 없습니다")
    } else {
        LazyColumn {
            items(postList) { post ->
                GuildPost(post)
            }
        }
    }
}

@Composable
fun GuildPost(post: GuildPostResponse) {
    Column(modifier = Modifier.height(150.dp)) {
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
                androidx.compose.material3.Text(
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
                androidx.compose.material3.Text(
                    text = "${post.likeCnt}",
                    style = MaterialTheme.typography.body2
                )
            }
            androidx.compose.material3.Text(
                text = formatTime(post.createdAt),
                style = MaterialTheme.typography.caption
            )
            androidx.compose.material3.Text(
                text = post.userNickName,
                style = MaterialTheme.typography.caption
            )
        }
    }
}
