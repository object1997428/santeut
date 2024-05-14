package com.santeut.data.model.request

data class CreateGuildPostRequest(

    val guildId: Int,
    val categoryId: Int,
    val guildPostTitle: String,
    val guildPostContent: String

)
