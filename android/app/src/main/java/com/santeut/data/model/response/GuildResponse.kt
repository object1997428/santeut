package com.santeut.data.model.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
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
