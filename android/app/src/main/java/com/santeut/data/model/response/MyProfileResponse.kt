package com.santeut.data.model.response

import com.google.gson.annotations.SerializedName

data class MyProfileResponse(

    @SerializedName("userId") val userId: Int,
    @SerializedName("userLoginId") val userLoginId: String,
    @SerializedName("userNickname") val userNickname: String,
    @SerializedName("userTierName") val userTierName: String,
    @SerializedName("userTierPoint") val userTierPoint: Int,
    @SerializedName("userDistance") val userDistance: Int,
    @SerializedName("userMoveTime") val userMoveTime: Int,
    @SerializedName("userHikingCount") val userHikingCount: Int,
    @SerializedName("userHikingMountain") val userHikingMountain: Int,
    @SerializedName("userProfile") val userProfile: String
)
