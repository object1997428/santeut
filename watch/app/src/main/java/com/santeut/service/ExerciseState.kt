package com.santeut.service

import android.util.Log
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.ExerciseState
import androidx.health.services.client.data.ExerciseUpdate.ActiveDurationCheckpoint
import androidx.health.services.client.data.LocationAvailability
import androidx.health.services.client.data.LocationData

data class ExerciseMetrics(
    val heartRate: Double? = null,
    val distance: Double? = null,
    val stepsTotal: Long? = null,
    val calories: Double? = null,
    val heartRateAverage: Double? = null,
    val absoluteElevation: Double? = null,
    val elevationGainTotal: Double? = null,
    val location: LocationData? = null
) {
    fun update(latestMetrics: DataPointContainer): ExerciseMetrics {
//        Log.d("HEART_RATE_BPM", "HEART_RATE_BPM : " + latestMetrics.getData(DataType.HEART_RATE_BPM).lastOrNull()?.value.toString())
//        Log.d("DISTANCE_TOTAL", "DISTANCE_TOTAL : " + latestMetrics.getData(DataType.DISTANCE_TOTAL)?.total.toString())
//        Log.d("STEPS_TOTAL", "STEPS_TOTAL : " + latestMetrics.getData(DataType.STEPS_TOTAL)?.total.toString())
//        Log.d("CALORIES_TOTAL", "CALORIES_TOTAL : " + latestMetrics.getData(DataType.CALORIES_TOTAL)?.total.toString())
//        Log.d("ABSOLUTE_ELEVATION", "ABSOLUTE_ELEVATION : " + latestMetrics.getData(DataType.ABSOLUTE_ELEVATION).lastOrNull()?.value.toString())
//        Log.d("ELEVATION_GAIN_TOTAL", "ELEVATION_GAIN_TOTAL : " + latestMetrics.getData(DataType.ELEVATION_GAIN_TOTAL)?.total.toString())
//        Log.d("LOCATION", "LOCATION : " + latestMetrics.getData(DataType.LOCATION).lastOrNull()?.value.toString())

        if (latestMetrics.getData(DataType.LOCATION).lastOrNull() != null) {
            Log.d("위치 정보",
                latestMetrics.getData(DataType.LOCATION).lastOrNull()?.value?.latitude.toString()
                + " / "
                + latestMetrics.getData(DataType.LOCATION).lastOrNull()?.value?.longitude.toString()
            )
        }

        return copy(
            heartRate = latestMetrics.getData(DataType.HEART_RATE_BPM).lastOrNull()?.value
                ?: heartRate,
            distance = latestMetrics.getData(DataType.DISTANCE_TOTAL)?.total ?: distance,
            stepsTotal = latestMetrics.getData(DataType.STEPS_TOTAL)?.total ?: stepsTotal,
            calories = latestMetrics.getData(DataType.CALORIES_TOTAL)?.total ?: calories,
            heartRateAverage = latestMetrics.getData(DataType.HEART_RATE_BPM_STATS)?.average
                ?: heartRateAverage,
            absoluteElevation = latestMetrics.getData(DataType.ABSOLUTE_ELEVATION)
                .lastOrNull()?.value
                ?: absoluteElevation,
            elevationGainTotal = latestMetrics.getData(DataType.ELEVATION_GAIN_TOTAL)?.total
                ?: elevationGainTotal,
            location = latestMetrics.getData(DataType.LOCATION).lastOrNull()?.value ?: location
        )
    }
}

data class  ExerciseServiceState(
    val exerciseState: ExerciseState? = null,
    val exerciseMetrics: ExerciseMetrics = ExerciseMetrics(),
    val exerciseLaps: Int = 0,
    val activeDurationCheckpoint: ActiveDurationCheckpoint? = null,
    val locationAvailability: LocationAvailability = LocationAvailability.UNKNOWN,
    val error: String? = null,
)