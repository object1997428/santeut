package com.santeut.domain.usecase

import com.santeut.data.model.response.ChatRoomInfo
import com.santeut.data.repository.ChatRepository
import javax.inject.Inject

class ChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {

    suspend fun getChatRoomList(): List<ChatRoomInfo> = chatRepository.getChatList()

    suspend fun getChatMessageList(
        partyId: Int
    ) = chatRepository.getChatMessageList(partyId)

}