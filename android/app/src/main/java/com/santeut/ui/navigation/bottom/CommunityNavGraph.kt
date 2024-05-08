package com.santeut.ui.navigation.bottom

import CommunityScreen
import CreatePostScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.santeut.ui.community.CommonViewModel
import com.santeut.ui.community.tips.PostTipsScreen
import com.santeut.ui.community.PostViewModel
import com.santeut.ui.community.common.ReadPostScreen
import com.santeut.ui.navigation.top.TopBar

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
        composable("postTips") {
            PostTipsScreen(navController)
        }
        composable(
            route = "createPost/{postType}",
            arguments = listOf(navArgument("postType") { type = NavType.StringType })
        ) { backStackEntry ->
            val postType = backStackEntry.arguments?.getString("postType") ?: "T"
            val postViewModel = hiltViewModel<PostViewModel>()
            CreatePostScreen(navController, postViewModel, postType.first())
        }
        composable(
            route = "readPost/{postId}/{postType}",
            arguments = listOf(
                navArgument("postId") { type = NavType.IntType },
                navArgument("postType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getInt("postId") ?: 0
            val postType = backStackEntry.arguments?.getString("postType") ?: "T"
            val postViewModel = hiltViewModel<PostViewModel>()
            val commonViewModel = hiltViewModel<CommonViewModel>()
            ReadPostScreen(postId, postType.first(), postViewModel, commonViewModel, navController)
        }
    }
}