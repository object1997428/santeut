package com.ssafy.santeut.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ssafy.santeut.ui.home.HomeScreen

@Composable
fun SanteutNavGraph (
    navHostController: NavHostController
){
    NavHost(
        navController = navHostController,
        startDestination = "unauth"
        ) {
        UnAuthNavGraph(navHostController)
        composable("home"){
            HomeScreen()
        }
    }

}