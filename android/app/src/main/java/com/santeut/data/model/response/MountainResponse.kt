package com.santeut.data.model.response

import com.google.gson.annotations.SerializedName


data class MountainListResponse(
    @SerializedName("result") val result: List<MountainResponse>
)

data class MountainResponse(

    @SerializedName("mountainId") val mountainId: Int,
    @SerializedName("mountainName") val mountainName: String,
    @SerializedName("regionName") val regionName: String,
    @SerializedName("height") val height: Int,
    @SerializedName("courseCount") val courseCount: Int,
    @SerializedName("isTop100") val isTop100: Boolean,
    @SerializedName("image") val image: String

)
