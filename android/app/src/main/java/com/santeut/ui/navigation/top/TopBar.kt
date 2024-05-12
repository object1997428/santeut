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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Share
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
    when (currentTap) {
        "home" -> HomeTopBar(navController)
        "community", "readPost" -> DefaultTopBar(navController, "커뮤니티")
        "guild" -> DefaultTopBar(navController, "나의 모임")
        "mypage" -> DefaultTopBar(navController, "마이페이지")
        "chatList" -> SimpleTopBar(navController, "채팅방")
        "noti" -> SimpleTopBar(navController, "알림")
        "mountain/{mountainId}" -> SimpleTopBar(navController, "산 정보")
        "createParty" -> SimpleTopBar(navController, "소모임 만들기")
        "chatRoom/{partyId}" -> MenuTopBar(navController, "소모임 제목")
    }
}

@Composable
fun HomeTopBar(
    navController: NavController
) {
    TopAppBar(
        title = { Text(text = "산뜻") },
        contentColor = Color.Black,
        backgroundColor = Color.White,
        navigationIcon = {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .size(50.dp)
                    .padding(start = 16.dp)
                    .clickable(onClick = { navController.navigate("home") })
            )
        },
        actions = {
            IconButton(onClick = { navController.navigate("chatList") }) {
                Icon(
                    imageVector = Icons.Outlined.Message,
                    contentDescription = "Message"
                )
            }
            IconButton(onClick = { navController.navigate("noti") }) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications"
                )
            }
        }
    )
}

@Composable
fun DefaultTopBar(navController: NavController, pageName: String) {
    TopAppBar(
        title = { Text(pageName) },
        contentColor = Color.Black,
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
            IconButton(onClick = { navController.navigate("chatList") }) {
                Icon(
                    imageVector = Icons.Outlined.Message,
                    contentDescription = "Message"
                )
            }
            IconButton(onClick = { navController.navigate("noti") }) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications"
                )
            }
        }
    )
}

@Composable
fun SimpleTopBar(navController: NavController, pageName: String) {
    TopAppBar(
        title = { Text(pageName) },
        contentColor = Color.Black,
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
        }
    )
}

@Composable
fun MenuTopBar(navController: NavController, pageName: String) {
    TopAppBar(
        title = { Text(pageName) },
        contentColor = Color.Black,
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
            IconButton(onClick = { /* 클릭 시 메뉴 열림 */ }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "추가 메뉴"
                )
            }
        }
    )
}

@Composable
fun CreateTopBar(navController: NavController, pageName: String, onWriteClick: () -> Unit) {
    TopAppBar(
        title = { Text(pageName) },
        contentColor = Color.Black,
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
            IconButton(onClick = onWriteClick) {
                Icon(
                    imageVector = Icons.Outlined.Create,
                    contentDescription = "글쓰기"
                )
            }
        }
    )
}

@Composable
fun GuildTopBar(navController: NavController, pageName: String) {
    TopAppBar(
        title = { Text(pageName) },
        contentColor = Color.Black,
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
            IconButton(onClick = { /* 클릭 시 링크 공유 */ }) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = "링크 공유"
                )
            }
            IconButton(onClick = { /* 클릭 시 메뉴 열림 */ }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "추가 메뉴"
                )
            }
        }
    )
}