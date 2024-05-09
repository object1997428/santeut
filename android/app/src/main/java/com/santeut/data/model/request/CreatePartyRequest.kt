package com.santeut.data.model.request

data class CreatePartyRequest(

    val schedule: String,
    val partyName: String,
    val mountainId: Int,
    val mountainName: String,
    val maxPeople: Int,
    val guildId: Int?,
    val place: String,
    val selectedCourse: List<Int>

)