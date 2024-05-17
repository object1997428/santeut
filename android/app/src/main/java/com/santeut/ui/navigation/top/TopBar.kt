package com.santeut.ui.navigation.top

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.santeut.R
import com.santeut.data.model.response.GuildResponse
import com.santeut.ui.guild.GuildViewModel
import com.santeut.ui.party.PartyViewModel

@Composable
fun TopBar(
    navController: NavController,
    currentTap: String?
) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    when (currentTap) {
        "home" -> HomeTopBar(navController)
        "community/{initialPage}", "community" -> DefaultTopBar(navController, "커뮤니티")
        "guild" -> DefaultTopBar(navController, "나의 모임")
        "mypage" -> DefaultTopBar(navController, "마이페이지")
        "chatList" -> SimpleTopBar(navController, "채팅방")
        "noti" -> SimpleTopBar(navController, "알림")
        "mountain/{mountainId}" -> SimpleTopBar(navController, "산 정보")
        "createGuild"-> SimpleTopBar(navController, "동호회 만들기")
        "createParty" -> SimpleTopBar(navController, "소모임 만들기")
        "chatRoom/{partyId}/{partyName}" -> {
            MenuTopBar(
                navController,
                currentBackStackEntry?.arguments?.getString("partyName") ?: "소모임 제목"
            )
        }
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
//        actions = {
//            IconButton(onClick = { navController.navigate("chatList") }) {
//                Icon(
//                    imageVector = Icons.Outlined.Message,
//                    contentDescription = "Message"
//                )
//            }
//            IconButton(onClick = { navController.navigate("noti") }) {
//                Icon(
//                    imageVector = Icons.Outlined.Notifications,
//                    contentDescription = "Notifications"
//                )
//            }
//        }
//        Spacer(modifier = Modifier.width(2.dp))
//        Text(
//            text = "산뜻",
//            fontSize = 24.sp,
//            fontWeight = FontWeight.ExtraBold
//        )
//        Spacer(modifier = Modifier.weight(1f))
//        Image(
//            imageVector = Icons.AutoMirrored.Outlined.Message,
//            contentDescription = "채팅",
//            modifier = Modifier
//                .padding(10.dp)
//                .clickable { onClickChatting() }
//        )
//        Spacer(modifier = Modifier.width(4.dp))
//        Image(
//            imageVector = Icons.Outlined.Notifications,
//            contentDescription = "알림",
//            modifier = Modifier
//                .clickable { onClickAlert() }
//        )
//        Spacer(modifier = Modifier.width(8.dp))
//    }
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
                    imageVector = Icons.AutoMirrored.Outlined.Message,
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
fun MenuTopBar(
    navController: NavController,
    pageName: String,
    partyViewModel: PartyViewModel = hiltViewModel()
) {

    var showMenu by remember { mutableStateOf(false) }

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

            var showDialog by remember { mutableStateOf(false) }

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
                    text = { Text(text = "소모임 정보") },
                    // TODO: 소모임 상세 조회
                    onClick = { Log.d("소모임 정보", "클릭") }
                    // onClick = { navController.navigate("guildMemberList/${guild.guildId}") }
                )

                DropdownMenuItem(
                    text = { Text(text = "소모임 나가기", color = Color.Red) },
                    onClick = { showDialog = true })
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    text = { Text(text = "${pageName}에서 나가시겠습니까?") },
                    confirmButton = {
                        Button(
                            onClick = {
//                                partyViewModel.// TODO: 소모임 나가기
                                Log.d("소모임", "나감")
                                showDialog = false
                            }
                        ) {
                            Text("나가기")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("취소")
                        }
                    }
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
fun GuildTopBar(
    navController: NavController,
    guild: GuildResponse,
    guildViewModel: GuildViewModel = hiltViewModel()
) {

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

            var showDialog by remember { mutableStateOf(false) }

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
                    DropdownMenuItem(
                        text = { Text(text = "가입 요청 보기") },
                        onClick = { navController.navigate("guildApplyList/${guild.guildId}") })
                    DropdownMenuItem(text = { Text(text = "동호회 정보 수정") }, onClick = { /*TODO*/ })
                }

                DropdownMenuItem(
                    text = { Text(text = "동호회 탈퇴하기", color = Color.Red) },
                    onClick = { showDialog = true })
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    text = { Text(text = "${guild.guildName}을 탈퇴할까요?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                guildViewModel.quitGuild(guild.guildId)
                                showDialog = false
                            }
                        ) {
                            Text("탈퇴")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("취소")
                        }
                    }
                )
            }
        }
    )
}