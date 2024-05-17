package com.santeut.ui.map

import com.santeut.domain.usecase.HikingUseCase
import com.santeut.domain.usecase.PartyUseCase
import com.santeut.ui.party.PartyViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.WebSocket
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val partyUseCase: PartyUseCase,
    private val hikingUseCase: HikingUseCase
) {

    // Web Socket
    var webSocket: WebSocket? = null
    private val webSocketUrl = "wss://k10e201.p.ssafy.io/api/hiking/chat/rooms/1"


}