package com.santeut.data.repository

import com.santeut.data.model.response.ChatMessage
import com.santeut.data.model.response.ChatRoomInfo


interface ChatRepository {

    suspend fun getChatList(): List<ChatRoomInfo>

    suspend fun getChatMessageList(partyId: Int): List<ChatMessage>
}