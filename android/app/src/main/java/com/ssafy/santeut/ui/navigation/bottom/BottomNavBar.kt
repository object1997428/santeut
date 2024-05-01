package com.ssafy.santeut.ui.navigation.bottom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
<<<<<<< HEAD
=======
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
>>>>>>> 070aae5b7e21058a8d65a52b920d7911e350f8a5
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
<<<<<<< HEAD
import androidx.compose.foundation.layout.fillMaxSize
=======
import androidx.compose.foundation.layout.fillMaxHeight
>>>>>>> 070aae5b7e21058a8d65a52b920d7911e350f8a5
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
<<<<<<< HEAD
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.BottomNavigationItem
=======
import androidx.compose.foundation.selection.selectable
>>>>>>> 070aae5b7e21058a8d65a52b920d7911e350f8a5
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
<<<<<<< HEAD
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp


=======
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.santeut.R
import com.ssafy.santeut.designsystem.theme.SanteutTheme


@Preview
@Composable
fun BottomNavBarPreview(
) {
    SanteutTheme {
        BottomNavBar(
            currentTap = "Home",
            onTabClick = {}
        )
    }
}

>>>>>>> 070aae5b7e21058a8d65a52b920d7911e350f8a5
@Composable
fun BottomNavBar(
    currentTap: String?,
    onTabClick: (BottomTap) -> Unit
) {

    val NoBottomScreen = listOf("landing", "login", "signup")

    AnimatedVisibility(
<<<<<<< HEAD
        visible = currentTap != null && !NoBottomScreen.contains(currentTap),
        enter = fadeIn() + slideIn { IntOffset(0, -it.height) },
        exit = fadeOut() + slideOut { IntOffset(0, -it.height) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
=======
        modifier = Modifier
            .background(color = Color.White)
            .padding(8.dp),
        visible = currentTap != null && !NoBottomScreen.contains(currentTap),
        enter = fadeIn() + slideIn { IntOffset(it.width, 0) },
        exit = fadeOut() + slideOut { IntOffset(it.width, 0) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(12.dp, 0.dp, 12.dp, 0.dp),
>>>>>>> 070aae5b7e21058a8d65a52b920d7911e350f8a5
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BottomTap.entries.forEach { tab ->
                BottomBarItem(
                    tab = tab,
                    selected = currentTap == tab.description,
                    onClick = { onTabClick(tab) }
                )
            }
        }
    }
}

@Composable
fun RowScope.BottomBarItem(
    tab: BottomTap,
    selected: Boolean,
    onClick: () -> Unit
) {
<<<<<<< HEAD
    Box(
        modifier = Modifier
            .selectable(
                selected = selected,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ){
            Icon(
                imageVector = tab.icon,
                contentDescription = tab.description
            )
            Spacer(
                modifier = Modifier
                    .height(2.dp)
            )
            Text(tab.title)
        }
    }
=======
    if (tab.title != "지도") {
        Box(
            modifier = Modifier
                .width(60.dp)
                .selectable(
                    selected = selected,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = tab.icon,
                    contentDescription = tab.description
                )
                Spacer(
                    modifier = Modifier
                        .height(2.dp)
                )
                Text(
                    text = tab.title,
                    fontSize = 12.sp
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
                .width(80.dp)
                .selectable(
                    selected = selected,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.map_bottom_tap),
                contentDescription = "map_bottom_tap",
                contentScale = ContentScale.Crop,

                modifier = Modifier
                    .fillMaxHeight()
            )
        }
    }

>>>>>>> 070aae5b7e21058a8d65a52b920d7911e350f8a5
}

enum class BottomTap(
    val icon: ImageVector,
    val route: String,
    val title: String,
    val description: String
) {
    HOME(
        icon = Icons.Default.Home,
        route = "home_graph",
        title = "홈",
        description = "home"
    ),
    COMMUNITY(
        icon = Icons.Default.Mail,
        route = "community_graph",
        title = "커뮤니티",
        description = "community"
    ),
    MAP(
        icon = Icons.Default.LocationOn,
        route = "map_graph",
        title = "지도",
        description = "map"
    ),
    GUILD(
        icon = Icons.Default.AccountBalance,
        route = "guild_graph",
        title = "동호회",
        description = "guild"
    ),
    MYPAGE(
        icon = Icons.Default.Person,
        route = "mypage_graph",
        title = "마이페이지",
        description = "mypage"
    )
}