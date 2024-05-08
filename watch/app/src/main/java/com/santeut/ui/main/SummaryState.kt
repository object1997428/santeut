package com.santeut.ui.main

import java.time.Duration

data class SummaryState (
    val averageHeartRate: Double,
    val totalDistance: Double,
    val totalStepsTotal: Long,
    val totalCalories: Double,
    val elapsedTime: Duration,
    val elevationGainTotal: Double,
)