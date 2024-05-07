package com.santeut.ui.navigation.bottom

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.santeut.ui.map.MapScreen



fun NavGraphBuilder.MapNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = "map",
        route = "map_graph"
    ) {
        composable("map") {
            val context = LocalContext.current
            MapScreen(context = context)
        }
    }
}