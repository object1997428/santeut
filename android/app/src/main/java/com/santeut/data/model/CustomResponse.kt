package com.santeut.data.model

import com.google.gson.annotations.SerializedName

data class CustomResponse<out T>(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: T
)