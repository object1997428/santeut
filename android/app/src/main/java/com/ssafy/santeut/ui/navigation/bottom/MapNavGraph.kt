package com.ssafy.santeut.ui.navigation.bottom

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ssafy.santeut.ui.community.CommunityScreen
import com.ssafy.santeut.ui.map.MapScreen


fun NavGraphBuilder.MapNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = "map",
        route = "map_graph"
    ) {
        composable("map") {
            MapScreen()
        }
    }
}