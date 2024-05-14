package com.santeut.data.model.response

import com.google.gson.annotations.SerializedName


data class PartyListResponse(
    @SerializedName("content") val partyList: List<PartyResponse>
)

data class PartyResponse(

    @SerializedName("partyId") val partyId: Int,
    @SerializedName("partyName") val partyName: String,
    @SerializedName("guildName") val guildName: String,
    @SerializedName("schedule") val schedule: String,
    @SerializedName("mountainName") val mountainName: String,
    @SerializedName("place") val place: String,
    @SerializedName("maxPeople") val maxPeople: Int,
    @SerializedName("curPeople") val curPeople: Int,
    @SerializedName("owner") val owner: String,
    @SerializedName("status") val status: String,
    @SerializedName("isMember") val isMember: Boolean

)

data class MyPartyListResponse(
    @SerializedName("content") val partyList: List<MyPartyResponse>
)

data class MyPartyResponse(
    @SerializedName("partyId") val partyId: Int,
    @SerializedName("partyUserId") val partyUserId: Int,
    @SerializedName("partyName") val partyName: String,
    @SerializedName("guildName") val guildName: String,
    @SerializedName("schedule") val schedule: String,
    @SerializedName("mountainName") val mountainName: String,
    @SerializedName("place") val place: String,
    @SerializedName("maxPeople") val maxPeople: Int,
    @SerializedName("curPeople") val curPeople: Int,
    @SerializedName("owner") val owner: String,
    @SerializedName("status") val status: String,
)
