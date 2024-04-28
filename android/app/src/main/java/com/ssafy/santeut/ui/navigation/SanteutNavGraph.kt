package com.ssafy.santeut.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ssafy.santeut.ui.navigation.bottom.CommunityNavGraph
import com.ssafy.santeut.ui.navigation.bottom.GuildNavGraph
import com.ssafy.santeut.ui.navigation.bottom.HomeNavGraph
import com.ssafy.santeut.ui.navigation.bottom.MapNavGraph
import com.ssafy.santeut.ui.navigation.bottom.MyPageNavGraph

@Composable
fun SanteutNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "auth_graph"
    ) {
        UnAuthNavGraph(navController)
        HomeNavGraph(navController)
        CommunityNavGraph(navController)
        MapNavGraph(navController)
        GuildNavGraph(navController)
        MyPageNavGraph(navController)
    }
}