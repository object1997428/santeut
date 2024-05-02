package com.ssafy.santeut.ui.navigation.bottom

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ssafy.santeut.ui.community.CommunityScreen
import com.ssafy.santeut.ui.map.MapScreen


fun NavGraphBuilder.MapNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = "map",
        route = "map_graph"
    ) {
        composable("map") {
            // Composable 함수에서 Context를 직접적으로 참조할 수 있음
            val context = LocalContext.current
            MapScreen(context = context)
        }
    }
}
