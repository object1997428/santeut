package com.santeut.data.repository

import com.santeut.data.apiservice.UserApiService
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService
) : UserRepository {
}