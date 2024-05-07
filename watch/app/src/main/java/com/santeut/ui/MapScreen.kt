package com.santeut.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.health.services.client.data.LocationData
import androidx.wear.compose.material.Text

@Composable
fun MapScreen(
) {
    Column (
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "지도야 지도야")
//        Text(text = location.latitude.toString())
//        Text(text = location.longitude.toString())
//        Text(text = location.altitude.toString())
//        Text(text = location.bearing.toString())
    }
}