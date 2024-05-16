@file:OptIn(ExperimentalNaverMapApi::class)

package com.santeut.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.santeut.ui.navigation.top.TopNavGraph
import com.santeut.ui.navigation.bottom.CommunityNavGraph
import com.santeut.ui.navigation.bottom.GuildNavGraph
import com.santeut.ui.navigation.bottom.HomeNavGraph
import com.santeut.ui.navigation.bottom.MapNavGraph
import com.santeut.ui.navigation.bottom.MountainNavGraph
import com.santeut.ui.navigation.bottom.MyPageNavGraph
import com.santeut.ui.wearable.WearableViewModel

@Composable
fun SanteutNavGraph(
    navController: NavHostController,
    wearableViewModel: WearableViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "auth_graph"
    ) {
        UnAuthNavGraph(
            navController = navController
        )
        HomeNavGraph(navController)
        CommunityNavGraph(navController)
        MapNavGraph(navController)
        GuildNavGraph(navController)
        MyPageNavGraph(navController)
        TopNavGraph(navController)
        MountainNavGraph(navController)
    }
}