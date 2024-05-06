package com.santeut

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.santeut.ui.HealthScreen
import com.santeut.ui.MainScreen
import com.santeut.ui.MapScreen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SanteutApp(
    onMinimize: () -> Boolean
) {
    val pagerState = rememberPagerState(
        pageCount = { 3 }
    )
    val navController = rememberSwipeDismissableNavController()

    HorizontalPager(
        modifier = Modifier
            .fillMaxSize(),
        state = pagerState
    ) {
        page ->
        when (page) {
            0 -> MainScreen()
            1 -> HealthScreen()
            2 -> MapScreen()
        }
    }
}
