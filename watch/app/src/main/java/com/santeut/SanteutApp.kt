package com.santeut

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.santeut.data.HealthServicesRepository
import com.santeut.ui.HealthScreen
import com.santeut.ui.HealthViewModel
import com.santeut.ui.HealthViewModelFactory
import com.santeut.ui.MainScreen
import com.santeut.ui.MapScreen

@OptIn(ExperimentalPermissionsApi::class, ExperimentalFoundationApi::class)
@Composable
fun SanteutApp(
    healthServicesRepository: HealthServicesRepository
) {
    val viewModel: HealthViewModel = viewModel(
        factory = HealthViewModelFactory(
            healthServicesRepository = healthServicesRepository
        )
    )

    val enabled by viewModel.enabled.collectAsState()

    val permissionState = rememberPermissionState(
        permission = PERMISSION,
        onPermissionResult = { granted ->
//            if (granted) viewModel.toggleEnabled()
        }
    )

    val pagerState = rememberPagerState(
        pageCount = { 3 }
    )

    HorizontalPager(
        modifier = Modifier
            .fillMaxSize(),
        state = pagerState
    ) {page ->
        Scaffold (
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black),
            timeText = { TimeText() }
        ){
            when (page) {
                0 -> MainScreen(
                    onToggle = {
                        if (permissionState.status.isGranted) {
                            viewModel.toggleEnabled()
                        }
                    },
                    state = enabled
                )
                1 -> HealthScreen(
                    healthServicesRepository = healthServicesRepository,
                    viewModel = viewModel,
                    permissionState = permissionState
                )
                2 -> MapScreen()
            }
        }
    }
}
