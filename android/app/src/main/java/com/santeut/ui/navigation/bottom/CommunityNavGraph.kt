package com.santeut.ui.navigation.bottom

import CommunityScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.santeut.ui.community.CreatePostScreen
import com.santeut.ui.community.PostTipsScreen
import com.santeut.ui.guild.GuildScreen


fun NavGraphBuilder.CommunityNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = "community",
        route = "community_graph"
    ) {
        composable("community") {
            CommunityScreen(navController)
        }
        composable("postTips"){
            PostTipsScreen(navController)
        }
        composable("createPost"){
            CreatePostScreen(navController)
        }

    }
}