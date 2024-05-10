package com.example.testpositions

data class LocationMessage(
    val partyId: Int,
    val userId: Int,
    val userNickname: String,
    val lat: String,
    val lng: String
)
