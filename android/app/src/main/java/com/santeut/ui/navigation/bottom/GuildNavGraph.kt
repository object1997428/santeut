package com.santeut.ui.navigation.bottom

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.santeut.data.model.response.GuildResponse
import com.santeut.ui.guild.CreateGuildPostScreen
import com.santeut.ui.guild.CreateGuildScreen
import com.santeut.ui.guild.GuildApplyListScreen
import com.santeut.ui.guild.GuildCommunityScreen
import com.santeut.ui.guild.GuildMemberListScreen
import com.santeut.ui.guild.GuildPostDetailScreen
import com.santeut.ui.guild.GuildScreen
import com.santeut.ui.guild.GuildViewModel
import com.santeut.ui.guild.MyGuildScreen
import com.santeut.ui.party.CreatePartyScreen


fun NavGraphBuilder.GuildNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = "guild",
        route = "guild_graph"
    ) {

        // 나의 모임 페이지
        composable("guild") {
            MyGuildScreen(navController)
        }

        composable("createGuild") {
            CreateGuildScreen()
        }

        // 동호회 상세 페이지
        composable(
            route = "getGuild/{guildId}",
            arguments = listOf(
                navArgument("guildId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val guildId = backStackEntry.arguments?.getInt("guildId") ?: 0
            GuildScreen(guildId, navController)
        }

        // 동호회 게시판
        composable(
            route = "guildCommunity/{guildId}",
            arguments = listOf(navArgument("guildId") { type = NavType.IntType })
        ) { backStackEntry ->
            val guildId = backStackEntry.arguments?.getInt("guildId") ?: 0
            GuildCommunityScreen(guildId, navController)
        }

        // 동호회 게시판 글쓰기
        composable(
            route = "createGuildPost/{guildId}",
            arguments = listOf(
                navArgument("guildId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val guildId = backStackEntry.arguments?.getInt("guildId") ?: 0
            CreateGuildPostScreen(guildId, navController)
        }

        // 동호회 게시글 상세조회
        composable(
            route = "getGuildPost/{guildPostId}",
            arguments = listOf(
                navArgument("guildPostId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val guildPostId = backStackEntry.arguments?.getInt("guildPostId") ?: 0
            GuildPostDetailScreen(guildPostId, navController)
        }

        // 동호회 회원 조회
        composable(
            route = "guildMemberList/{guildId}",
            arguments = listOf(
                navArgument("guildId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val guildId = backStackEntry.arguments?.getInt("guildId") ?: 0
            GuildMemberListScreen(guildId, navController)
        }

        // 동호회 가입 신청 목록 조회
        composable(
            route = "guildApplyList/{guildId}",
            arguments = listOf(
                navArgument("guildId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val guildId = backStackEntry.arguments?.getInt("guildId") ?: 0
            GuildApplyListScreen(guildId, navController)
        }

        // 동호회 전용 소모임 생성
        composable("createParty") {
            CreatePartyScreen(navController)
        }
    }
}