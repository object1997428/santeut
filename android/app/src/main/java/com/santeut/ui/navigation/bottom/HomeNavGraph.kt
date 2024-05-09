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
        composable(
            route = "mountainList/{name}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            MountainListScreen(name, null, navController)
        }
        composable(
            route = "mountainList/{name}/{region}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("region") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val region = backStackEntry.arguments?.getString("region")
            MountainListScreen(name, region, navController)
        }
    }
}
