package com.ssafy.santeut.ui.navigation.bottom

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ssafy.santeut.ui.guild.GuildScreen
import com.ssafy.santeut.ui.mypage.MyPageScreen


fun NavGraphBuilder.MyPageNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = "mypage",
        route = "mypage_graph"
    ) {
        composable("mypage") {
            MyPageScreen()
        }
    }
}