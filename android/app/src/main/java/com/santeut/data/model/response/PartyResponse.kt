package com.santeut.data.model.response

import com.google.gson.annotations.SerializedName


data class PartyListResponse(
    @SerializedName("partyList") val partyList: List<PartyResponse>
)

data class PartyResponse(
    @SerializedName("partyId") val partyId: Int
)
