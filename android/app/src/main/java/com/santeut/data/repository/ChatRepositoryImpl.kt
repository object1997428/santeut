package com.santeut.data.repository

import android.util.Log
import com.santeut.data.apiservice.ChatApiService
import com.santeut.data.model.response.ChatMessage
import com.santeut.data.model.response.ChatRoomInfo
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatApiService: ChatApiService
): ChatRepository {

    override suspend fun getChatList(): List<ChatRoomInfo> {
        return try {
            val response = chatApiService.getMyChatList()
            if(response.status=="200") {
                response.data.chatRoomList ?: emptyList()
            } else {
                Log.e(
                    "ChatRepository",
                    "채팅방 목록 조회 실패: ${response.status} - ${response.data}"
                )
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("ChatRepository", "Network error while fetching get: ${e.message}", e)
            throw e
        }

    }

    override suspend fun getChatMessageList(partyId: Int): List<ChatMessage> {
        return try {
            val response = chatApiService.getChatMessageList(partyId)
            if(response.status=="200") {
                response.data.chatMessageList ?: emptyList()
            } else {
                Log.e(
                    "ChatRepository",
                    "채팅방 대화내용 조회 실패: ${response.status} - ${response.data}"
                )
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("ChatRepository", "Network error while fetching get: ${e.message}", e)
            throw e
        }
    }


}