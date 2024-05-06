package com.santeut.data.model.request

import java.util.Date

data class SignUpRequest(
    val userNickname: String,
    val userLoginId: String,
    val userPassword: String,
    val userBirth: String,
    val userGender: Char
)