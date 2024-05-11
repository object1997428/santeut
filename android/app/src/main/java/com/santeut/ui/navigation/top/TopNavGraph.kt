package com.santeut.ui.navigation.top

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.santeut.ui.chat.ChatListScreen
import com.santeut.ui.chat.ChatScreen

fun NavGraphBuilder.TopNavGraph(
    navController: NavController
) {
    navigation(
        startDestination = "chatList",
        route = "chat_graph"
    ) {
        composable("chatList") {
            ChatListScreen(navController)
        }

        composable(
            route = "chatRoom/{partyId}",
            arguments = listOf(navArgument("partyId") { type = NavType.IntType })
        ) {backStackEntry->
            val partyId = backStackEntry.arguments?.getInt("partyId")?:0
            ChatScreen(partyId)
        }
    }
}