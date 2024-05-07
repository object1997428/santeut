package com.santeut

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.santeut.data.HealthServicesRepository
import com.santeut.ui.HealthScreen
import com.santeut.ui.HealthViewModel
import com.santeut.ui.HealthViewModelFactory
import com.santeut.ui.MainScreen
import com.santeut.ui.MapScreen
import kotlinx.coroutines.launch

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

    val bodySensorsPermission = rememberPermissionState(permission = android.Manifest.permission.BODY_SENSORS)
    val fineLocationPermission = rememberPermissionState(permission = android.Manifest.permission.ACCESS_FINE_LOCATION)
    val bluetoothPermission = rememberPermissionState(permission = android.Manifest.permission.BLUETOOTH)
    val backgroundLocationPermission = rememberPermissionState(permission = android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch {
            bodySensorsPermission.launchPermissionRequest()
            if (bodySensorsPermission.status.isGranted) {
                fineLocationPermission.launchPermissionRequest()
                if (fineLocationPermission.status.isGranted) {
                    bluetoothPermission.launchPermissionRequest()
                    if (bluetoothPermission.status.isGranted) {
                        backgroundLocationPermission.launchPermissionRequest()
                    }
                }
            }
        }
    }




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
                        viewModel.toggleEnabled()
                    },
                    state = enabled
                )
                1 -> HealthScreen(
                    healthServicesRepository = healthServicesRepository,
                    viewModel = viewModel
                )
                2 -> MapScreen(
                )
            }
        }
    }
}
