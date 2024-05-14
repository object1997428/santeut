package com.santeut.data.model.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime


data class CommentResponse(
    @SerializedName("commentId") val commentId: Int,
    @SerializedName("userNickname") val userNickname: String,
    @SerializedName("commentContent") val commentContent: String,
    @SerializedName("createdAt") val createdAt: LocalDateTime,
)
