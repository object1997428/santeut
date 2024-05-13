package com.santeut.service

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.health.services.client.data.ExerciseState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.PutDataMapRequest
import com.santeut.data.ExerciseClientManager
import com.santeut.data.isExerciseInProgress
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.Duration
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class ExerciseService : LifecycleService() {

    @Inject
    lateinit var dataClient: DataClient

    @Inject
    lateinit var exerciseClientManager: ExerciseClientManager

    @Inject
    lateinit var exerciseNotificationManager: ExerciseNotificationManager

    @Inject
    lateinit var exerciseServiceMonitor: ExerciseServiceMonitor

    private var periodicSendJob: Job? = null

    private var isBound = false
    private var isStarted = false
    private val localBinder = LocalBinder()

    private val serviceRunningInForeground: Boolean
        get() = this.foregroundServiceType != ServiceInfo.FOREGROUND_SERVICE_TYPE_NONE

    private suspend fun isExerciseInProgress() =
        exerciseClientManager.exerciseClient.isExerciseInProgress()

    suspend fun prepareExercise() {
        exerciseClientManager.prepareExercise()
    }

    suspend fun startExercise() {
        if (!serviceRunningInForeground) {
            postOngoingActivityNotification()
        }

        exerciseClientManager.startExercise()
        startPeriodicDataSend()
    }

    private fun startPeriodicDataSend() {
        periodicSendJob = lifecycleScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val exerciseMetrics = exerciseServiceMonitor.exerciseServiceState.value.exerciseMetrics
                    toSend(exerciseMetrics)
                    Log.d("To Send Heart Rate", exerciseMetrics.heartRate.toString())
                    delay(2000)
                } catch (e: Exception) {
                    Log.e(TAG, "Error sending data: ${e.localizedMessage}")
                    delay(1000)
                }
            }
        }
    }

    suspend fun pauseExercise() {
        exerciseClientManager.pauseExercise()
    }

    suspend fun resumeExercise() {
        exerciseClientManager.resumeExercise()
    }

    suspend fun endExercise() {
        exerciseClientManager.endExercise()
        removeOngoingActivityNotification()
        stopPeriodicDataSend()
    }

    private fun stopPeriodicDataSend() {
        periodicSendJob?.cancel()
        periodicSendJob = null
    }


    fun markLap() {
        lifecycleScope.launch {
            exerciseClientManager.markLap()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        Log.d(TAG, "onStartCommand")

        if (!isStarted) {
            isStarted = true

            if (!isBound) {
                stopSelfIfNotRunning()
            }

            lifecycleScope.launch(Dispatchers.Default) {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    exerciseServiceMonitor.monitor()
                }
            }
        }
        return START_STICKY
    }

    suspend fun toSend(exerciseMetrics: ExerciseMetrics) {
        try {
            val request = PutDataMapRequest.create("/health").apply {
                dataMap.apply {
                    exerciseMetrics.heartRate?.let { putDouble("heartRate", it) }
                    exerciseMetrics.distance?.let { putDouble("distance", it) }
                    exerciseMetrics.stepsTotal?.let { putLong("stepsTotal", it) }
                    exerciseMetrics.calories?.let { putDouble("calories", it) }
                    exerciseMetrics.heartRateAverage?.let { putDouble("heartRateAverage", it) }
                    exerciseMetrics.absoluteElevation?.let { putDouble("absoluteElevation", it) }
                    exerciseMetrics.elevationGainTotal?.let { putDouble("elevationGainTotal", it) }
                }
            }.asPutDataRequest().setUrgent()

            val result = dataClient.putDataItem(request).await()

            Log.d("toSend ", "DataItem saved: $result")
        } catch (cancellationException: CancellationException) {
            throw cancellationException
        } catch (exception: Exception) {
            Log.d("toSend ", "Saving DataItem failed: $exception")
        }
    }

    private fun stopSelfIfNotRunning() {
        lifecycleScope.launch {
            if (!isExerciseInProgress()) {
                if (exerciseServiceMonitor.exerciseServiceState.value.exerciseState == ExerciseState.PREPARING) {
                    lifecycleScope.launch {
                        endExercise()
                    }
                }
                stopSelf()
            }
        }
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)

        handleBind()

        return localBinder
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)

        handleBind()
    }

    private fun handleBind() {
        if (!isBound) {
            isBound = true
            startService(Intent(this, this::class.java))
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        isBound = false
        lifecycleScope.launch {
            delay(UNBIND_DELAY)
            if (!isBound) {
                stopSelfIfNotRunning()
            }
        }
        return true
    }

    fun removeOngoingActivityNotification() {
        if (serviceRunningInForeground) {
            Log.d(TAG, "Removing ongoing activity notification")
            stopForeground(STOP_FOREGROUND_REMOVE)
        }
    }

    private fun postOngoingActivityNotification() {
        if (!serviceRunningInForeground) {
            Log.d(TAG, "Posting ongoing activity notification")

            exerciseNotificationManager.createNotificationChannel()
            val serviceState = exerciseServiceMonitor.exerciseServiceState.value
            startForeground(
                ExerciseNotificationManager.NOTIFICATION_ID,
                exerciseNotificationManager.buildNotification(
                    serviceState.activeDurationCheckpoint?.activeDuration ?: Duration.ZERO
                )
            )
        } else {
            Log.d(TAG, "Service is already running in foreground")
        }
    }

    inner class LocalBinder : Binder() {
        fun getService() = this@ExerciseService

        val exerciseServiceState: Flow<ExerciseServiceState>
            get() = this@ExerciseService.exerciseServiceMonitor.exerciseServiceState
    }

    companion object {
        private val UNBIND_DELAY = 3.seconds
    }
}
