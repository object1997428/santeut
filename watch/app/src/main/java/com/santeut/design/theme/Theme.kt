package com.santeut.design.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.MaterialTheme

@Composable
fun SanteutTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content
    )
}