package com.santeut

import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.santeut.ui.HealthScreen
import com.santeut.ui.MainScreen
import com.santeut.ui.MapScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun SanteutApp(
    onFinishActivity: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val permissionStates = determinePermissions().map { permission ->
        rememberPermissionState(permission = permission)
    }

    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch {
            for (permissionState in permissionStates) {
                permissionState.launchPermissionRequest()
                while (!permissionState.status.shouldShowRationale && !permissionState.status.isGranted) {
                    delay(100)
                }
                if (permissionState.status.isGranted) continue
                else onFinishActivity()
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

                )
                1 -> HealthScreen(

                )
                2 -> MapScreen(

                )
            }
        }
    }
}

fun determinePermissions(): List<String> {
    val permissions = mutableListOf(
        android.Manifest.permission.BODY_SENSORS,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACTIVITY_RECOGNITION,
        android.Manifest.permission.FOREGROUND_SERVICE,
        android.Manifest.permission.BLUETOOTH
    )
    if (Build.VERSION.SDK_INT >= 33) {
        permissions.add(android.Manifest.permission.POST_NOTIFICATIONS)
    }
    return permissions
}