package com.ssafy.santeut.ui.navigation.bottom

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
<<<<<<< HEAD
import com.ssafy.santeut.ui.home.HomeSccreen
=======
import com.ssafy.santeut.ui.home.HomeScreen
>>>>>>> 070aae5b7e21058a8d65a52b920d7911e350f8a5


fun NavGraphBuilder.HomeNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = "home",
        route = "home_graph"
    ) {
        composable("home") {
<<<<<<< HEAD
            HomeSccreen()
=======
            HomeScreen()
>>>>>>> 070aae5b7e21058a8d65a52b920d7911e350f8a5
        }
    }
}
