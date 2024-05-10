package com.santeut.ui.navigation.bottom

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.santeut.ui.home.HomeScreen
import com.santeut.ui.mountain.MountainListScreen


fun NavGraphBuilder.HomeNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = "home",
        route = "home_graph"
    ) {
        composable("home") {
            HomeScreen(navController)
        }

    }
}
