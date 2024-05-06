package com.santeut

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.santeut.data.HealthServicesRepository
import com.santeut.ui.HealthScreen
import com.santeut.ui.MainScreen
import com.santeut.ui.MapScreen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SanteutApp(
//    healthServicesRepository: HealthServicesRepository
) {
    val pagerState = rememberPagerState(
        pageCount = { 3 }
    )

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
