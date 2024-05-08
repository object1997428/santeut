package com.santeut.service

import java.time.Duration

data class SummaryState (
    val averageHeartRate: Double,
    val totalDistance: Double,
    val totalCalories: Double,
    val elapsedTime: Duration,
)