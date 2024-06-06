package com.santeut.domain.usecase

import com.santeut.data.model.response.MyProfileResponse
import com.santeut.data.repository.UserRepository
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend fun getMyProfile(): MyProfileResponse{
        return userRepository.getMyProfile()
    }
}