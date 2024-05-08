package com.santeut.domain.usecase

import com.santeut.data.repository.UserRepository
import com.santeut.ui.mypage.UserViewModel
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val userRepository: UserRepository
){

}