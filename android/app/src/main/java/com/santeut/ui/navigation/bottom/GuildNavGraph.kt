package com.santeut.ui.navigation.bottom

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.santeut.ui.guild.GuildScreen


fun NavGraphBuilder.GuildNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = "guild",
        route = "guild_graph"
    ) {
        composable("guild") {
            GuildScreen(navController)
        }
    }
}