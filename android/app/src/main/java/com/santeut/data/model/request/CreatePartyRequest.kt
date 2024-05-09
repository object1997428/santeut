package com.santeut.data.model.request

data class CreatePartyRequest(

    val schedule: String,
    val partyName: String,
    val mountainId: String,
    val mountainName: String,
    val maxPeople: String,
    val guildId: String?,
    val place: String,
    val selectedCourse: List<Int>

)