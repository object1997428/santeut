package com.ssafy.santeut.ui.navigation.bottom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.BottomNavigationItem
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp


@Composable
fun BottomNavBar(
    currentTap: String?,
    onTabClick: (BottomTap) -> Unit
) {

    val NoBottomScreen = listOf("landing", "login", "signup")

    AnimatedVisibility(
        visible = currentTap != null && !NoBottomScreen.contains(currentTap),
        enter = fadeIn() + slideIn { IntOffset(0, -it.height) },
        exit = fadeOut() + slideOut { IntOffset(0, -it.height) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
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