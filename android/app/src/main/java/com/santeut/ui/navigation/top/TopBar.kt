package com.santeut.ui.navigation.top

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.santeut.R

@Composable
fun TopBar(
    navController: NavController,
    currentTap: String?
) {
    if (currentTap == "home") {
        HomeTopBar(
            onClickAlert = { /*TODO*/ },
            onClickChatting = { /*TODO*/ }
        )
    } else if (currentTap == "community") {
        CommunityTopBar(navController)
    }

    // TopBar 여기서 수정
}

@Composable
fun HomeTopBar(
    onClickAlert: () -> Unit,
    onClickChatting: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(12.dp)
            .background(color = Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .size(50.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
                contentScale = ContentScale.FillWidth,
            )
        }
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = "산뜻",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            imageVector = Icons.Outlined.Message,
            contentDescription = "채팅",
            modifier = Modifier
                .padding(10.dp)
                .clickable { onClickChatting() }
        )
        Spacer(modifier = Modifier.width(4.dp))
        Image(
            imageVector = Icons.Outlined.Notifications,
            contentDescription = "알림",
            modifier = Modifier
                .clickable { onClickAlert() }
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
fun CommunityTopBar(navController: NavController) {
    TopAppBar(
        title = { Text(text = "커뮤니티") },
        contentColor = MaterialTheme.colors.primary,
        backgroundColor = Color.White,
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.Message,
                    contentDescription = "Message"
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications"
                )
            }
        }
    )
}