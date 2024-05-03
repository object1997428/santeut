package com.santeut

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.santeut.ui.navigation.SanteutNavGraph
import com.santeut.ui.navigation.bottom.BottomNavBar

@Composable
fun SanteutApp(
) {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight(),
        bottomBar = {
            BottomNavBar(
                currentTap = navController.currentBackStackEntryAsState().value?.destination?.route,
                onTabClick = {
                    navController.navigate(it.route)
                }
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier.padding(padding)
            ) {
                SanteutNavGraph(navController)
            }
        }
    )
}
