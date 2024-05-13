package com.santeut.ui

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.PutDataMapRequest
import com.santeut.service.ExerciseMetrics
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class HealthDataViewModel(
    application: Application,
    private val dataClient: DataClient,
    private val messageClient: MessageClient,
    private val capabilityClient: CapabilityClient
) : AndroidViewModel(application), DataClient.OnDataChangedListener,
    MessageClient.OnMessageReceivedListener, CapabilityClient.OnCapabilityChangedListener {

    @SuppressLint("VisibleForTests")
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        Log.d("onDataChanged ", dataEvents.toString())
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        Log.d("onMessageReceived ", messageEvent.toString())
    }

    override fun onCapabilityChanged(capabilityInfo: CapabilityInfo) {
        Log.d("onCapabilityChanged ", capabilityInfo.toString())
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

//            Log.d("toSend ", "DataItem saved: $result")
        } catch (cancellationException: CancellationException) {
            throw cancellationException
        } catch (exception: Exception) {
            Log.d("toSend ", "Saving DataItem failed: $exception")
        }
    }
}

class HealthDataViewModelFactory(
    private val application: Application,
    private val dataClient: DataClient,
    private val messageClient: MessageClient,
    private val capabilityClient: CapabilityClient
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HealthDataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return HealthDataViewModel(
                application, dataClient, messageClient, capabilityClient
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}