package com.santeut.ui.navigation.bottom

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.santeut.ui.map.MapScreen


@ExperimentalNaverMapApi
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