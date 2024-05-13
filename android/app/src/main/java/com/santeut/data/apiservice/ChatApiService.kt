package com.santeut.data.apiservice

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.response.ChatResponse
import com.santeut.data.model.response.ChatRoomResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ChatApiService {

    @GET("/api/party/chat")
    suspend fun getMyChatList(): CustomResponse<ChatResponse>

    @GET("/api/party/chat/{partyId}")
    suspend fun getChatMessageList(
        @Path("partyId") partyId: Int
    ): CustomResponse<ChatRoomResponse>

}