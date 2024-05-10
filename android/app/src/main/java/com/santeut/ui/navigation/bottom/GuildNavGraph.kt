package com.santeut.ui.navigation.bottom

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.santeut.ui.guild.GuildScreen
import com.santeut.ui.guild.MyGuildScreen
import com.santeut.ui.party.CreatePartyScreen


fun NavGraphBuilder.GuildNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = "guild",
        route = "guild_graph"
    ) {
        composable("guild") {
            MyGuildScreen(navController)
        }
        composable(
            route = "getGuild/{guildId}",
            arguments = listOf(
                navArgument("guildId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val guildId = backStackEntry.arguments?.getInt("guildId") ?: 0
            GuildScreen(guildId)
        }
        composable("createParty"){
            CreatePartyScreen(navController)
        }
    }
}