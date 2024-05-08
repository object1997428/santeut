package com.santeut.ui.mypage

import androidx.lifecycle.ViewModel
import com.santeut.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): ViewModel(){
}