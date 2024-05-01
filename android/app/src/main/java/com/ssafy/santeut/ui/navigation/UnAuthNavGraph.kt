package com.ssafy.santeut.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ssafy.santeut.ui.landing.LandingScreen
import com.ssafy.santeut.ui.login.LoginScreen
import com.ssafy.santeut.ui.signup.SignUpScreen

fun NavGraphBuilder.UnAuthNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = "Landing",
        route = "auth_graph"
    ) {
        composable("landing") {
            LandingScreen(
                onNavigateLogin = {
                    navController.navigate(route = "login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateHome = {
<<<<<<< HEAD
                    navController.navigate(route = "home") {
=======
                    navController.navigate(route = "home_graph") {
>>>>>>> 070aae5b7e21058a8d65a52b920d7911e350f8a5
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable("login") {
            LoginScreen(
                onNavigateSignUp = {
                    navController.navigate(route = "signup")
                },
                onNavigateHome = {
<<<<<<< HEAD
                    navController.navigate(route = "home") {
=======
                    navController.navigate(route = "home_graph") {
>>>>>>> 070aae5b7e21058a8d65a52b920d7911e350f8a5
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable("signup") {
            SignUpScreen(
                onNavigateLogin = {
                    navController.navigate(route = "login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}