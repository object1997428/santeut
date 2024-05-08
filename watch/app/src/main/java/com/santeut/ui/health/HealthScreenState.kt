package com.santeut.ui.health

import com.santeut.data.ServiceState
import com.santeut.service.ExerciseServiceState
import com.santeut.ui.main.SummaryState
import java.time.Duration

data class HealthScreenState(
    val hasExerciseCapabilities: Boolean,
    val isTrackingAnotherExercise: Boolean,
    val serviceState: ServiceState,
    val exerciseState: ExerciseServiceState?
) {
    fun toSummary(): SummaryState {
        val exerciseMetrics = exerciseState?.exerciseMetrics
        val averageHeartRate = exerciseMetrics?.heartRateAverage ?: Double.NaN
        val totalDistance = exerciseMetrics?.distance ?: 0.0
        val totalSteps = exerciseMetrics?.stepsTotal ?: 0
        val totalCalories = exerciseMetrics?.calories ?: Double.NaN
        val duration = exerciseState?.activeDurationCheckpoint?.activeDuration ?: Duration.ZERO
        val elevationGainTotal = exerciseMetrics?.elevationGainTotal ?: 0.0
        return SummaryState(averageHeartRate, totalDistance, totalSteps, totalCalories, duration, elevationGainTotal)
    }

    val isEnding: Boolean
        get() = exerciseState?.exerciseState?.isEnding == true

    val isEnded: Boolean
        get() = exerciseState?.exerciseState?.isEnded == true

    val error: String?
        get() = when(serviceState) {
            is ServiceState.Connected -> serviceState.exerciseServiceState.error
            else -> null
        }
}