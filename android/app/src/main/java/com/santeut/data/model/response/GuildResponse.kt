package com.santeut.data.model.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class GuildListResponse(
    @SerializedName("guildList") val guildList: List<GuildResponse>
)

data class GuildResponse(
    @SerializedName("guildId") val guildId: Int,
    @SerializedName("guildName") val guildName: String,
    @SerializedName("guildProfile") val guildProfile: String,
    @SerializedName("guildInfo") val guildInfo: String,
    @SerializedName("guildMember") val guildMember: Int,
    @SerializedName("regionId") val regionId: Int,
    @SerializedName("guildGender") val guildGender: Char,
    @SerializedName("guildMinAge") val guildMinAge: Int,
    @SerializedName("guildMaxAge") val guildMaxAge: Int,
    @SerializedName("createdAt") val createdAt: LocalDateTime,
)

data class GuildPostListResponse(
    @SerializedName("postList") val postList: List<GuildPostResponse>
)

data class GuildPostResponse(
    @SerializedName("guildPostId") val guildPostId: Int,
    @SerializedName("categoryId") val categoryId: Int,
    @SerializedName("userNickName") val userNickName: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("guildPostTitle") val guildPostTitle: String,
    @SerializedName("guildPostContent") val guildPostContent: String,
    @SerializedName("createdAt") val createdAt: LocalDateTime,
    @SerializedName("likeCnt") val likeCnt: Int,
    @SerializedName("commentCnt") val commentCnt: Int,
    @SerializedName("hitCnt") val hitCnt: Int
)

