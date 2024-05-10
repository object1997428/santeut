package com.santeut.ui.navigation.bottom

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.santeut.ui.mountain.MountainListScreen
import com.santeut.ui.mountain.MountainScreen

fun NavGraphBuilder.MountainNavGraph(navController: NavController) {
    navigation(
        startDestination = "mountainList/{name}",
        route = "mountain_graph"
    ) {
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
        composable(route = "mountain/{mountainId}", arrayListOf(
            navArgument("mountainId") { type = NavType.IntType }
        )) { backStackEntry ->
            val mountainId = backStackEntry.arguments?.getInt("mountainId") ?: 0
            MountainScreen(mountainId)
        }
    }
}