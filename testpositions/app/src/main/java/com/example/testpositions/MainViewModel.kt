package com.example.testpositions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompMessage

class MainViewModel : ViewModel() {
    private var stompClient: StompClient? = null

    private val _otherUserLocations = MutableLiveData<List<LocationMessage>>()
    val otherUserLocations: LiveData<List<LocationMessage>> get() = _otherUserLocations

    fun connectWebSocket() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://k10e201.p.ssafy.io:52715/api/hiking/websocket")
        stompClient?.withClientHeartbeat(10000)?.withServerHeartbeat(10000)
        stompClient?.connect()

        // Lifecycle handling
        stompClient?.lifecycle()?.subscribe { lifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> println("Stomp connection opened")
                LifecycleEvent.Type.ERROR -> println("Error: " + lifecycleEvent.exception)
                LifecycleEvent.Type.CLOSED -> println("Stomp connection closed")
                else -> {}
            }
        }

        // Subscribe to location messages
        stompClient?.topic("/sub/websocket/room/1")?.subscribe { topicMessage ->
            handleReceivedMessage(topicMessage)
        }
    }

    private fun handleReceivedMessage(topicMessage: StompMessage) {
        val message = Gson().fromJson(topicMessage.payload, LocationMessage::class.java)
        updateMapWithLocation(message)
    }

    fun sendLocationMessage(latitude: Double, longitude: Double) {
        val message = LocationMessage(1, 3, "하이", latitude.toString(), longitude.toString())
        val jsonMessage = Gson().toJson(message)
        stompClient?.send("/pub/message", jsonMessage)?.subscribe()
    }

    fun disconnectWebSocket() {
        stompClient?.disconnect()
    }

    private fun updateMapWithLocation(message: LocationMessage) {
        val currentList = _otherUserLocations.value ?: listOf()
        _otherUserLocations.postValue(currentList + message)
    }
}
