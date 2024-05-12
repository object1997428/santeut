package com.santeut.ui.navigation.top

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.santeut.R
import com.santeut.data.model.response.GuildResponse

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
fun GuildTopBar(navController: NavController, guild: GuildResponse) {

    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(guild.guildName) },
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
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "추가 메뉴"
                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text(text = "회원 목록 보기") },
                    onClick = { navController.navigate("guildMemberList/${guild.guildId}") })
                DropdownMenuItem(text = { Text(text = "소모임 만들기") }, onClick = { /*TODO*/ })

                if (guild.isPresident) {
                    DropdownMenuItem(text = { Text(text = "가입 요청 보기") }, onClick = { /*TODO*/ })
                    DropdownMenuItem(text = { Text(text = "동호회 정보 수정") }, onClick = { /*TODO*/ })
                }

                DropdownMenuItem(
                    text = { Text(text = "동호회 탈퇴하기", color = Color.Red) },
                    onClick = { /*TODO*/ })
            }
        }
    )
}