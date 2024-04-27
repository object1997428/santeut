package com.ssafy.santeut.ui.landing

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.santeut.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {
    private var _state by mutableStateOf(UserState())
    val state = _state

    fun checkAuth() {
        viewModelScope.launch {
//            userUseCase.getToken()
//                .catch { e ->
//                    _state = _state.copy(token = "")
//                    Log.d("CheckAuth", "Error: ${e.message}")
//                }
//                .collectLatest { data ->
//                    _state = _state.copy(token = data)
//                }
        }
    }
}