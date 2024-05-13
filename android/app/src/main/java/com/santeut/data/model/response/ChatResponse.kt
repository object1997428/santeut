package com.santeut.data.model.response

import com.google.gson.annotations.SerializedName

data class ChatResponse (
    @SerializedName("chatRoom") val chatRoomList: List<ChatRoomInfo>
)

data class ChatRoomInfo (
    @SerializedName("partyId") val partyId: Int,
    @SerializedName("partyName") val partyName: String,
    @SerializedName("guildName") val guildName: String,
    @SerializedName("peopleCnt") val peopleCnt: Int,
    @SerializedName("lastMessage") val lastMessage: String?,
    @SerializedName("lastSentDate") val lastSentDate: String?
)

data class ChatRoomResponse(
    @SerializedName("chatMessage") val chatMessageList: List<ChatMessage>
)

data class ChatMessage(
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("senderNickname") val senderNickname: String,
    @SerializedName("senderProfile") val senderProfile: String?,
    @SerializedName("content") val content: String
)