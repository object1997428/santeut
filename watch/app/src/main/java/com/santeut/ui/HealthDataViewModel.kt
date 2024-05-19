package com.santeut.ui

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
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

    private val _userPositions = mutableStateOf<Map<String, LatLng>>(mapOf())
    val userPositions = _userPositions

    @SuppressLint("VisibleForTests")
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        val newUserPositions = mutableMapOf<String, LatLng>()

        dataEvents.forEach { dataEvent ->
            if (dataEvent.type == DataEvent.TYPE_CHANGED) {
                val dataItem = dataEvent.dataItem
                val path = dataItem.uri.path
                if (path.equals("/position")) {
                    Log.d("onDataChanged", "위치 변화 받음 ${dataItem.toString()}")
                    val dataMap = DataMapItem.fromDataItem(dataItem).dataMap

                    dataMap.keySet().forEach { key ->
                        if (key.endsWith("_latitude") || key.endsWith("_longitude")) {
                            val baseKey = key.substring(0, key.indexOf("_"))

                            val latitude = dataMap.getDouble(baseKey + "_latitude", 0.0)
                            val longitude = dataMap.getDouble(baseKey + "_longitude", 0.0)

                            newUserPositions[baseKey] = LatLng(latitude, longitude)
                            Log.d("user position", "$baseKey : $latitude / $longitude")
                        }
                    }

                    _userPositions.value = newUserPositions
                }
            }
        }
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