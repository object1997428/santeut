package com.ssafy.santeut.ui.login

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.santeut.data.model.request.LoginRequest
import com.ssafy.santeut.domain.usecase.LoginUseCase
import com.ssafy.santeut.ui.landing.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private var _userState = mutableStateOf(UserState())
    val userState = _userState

    private val _userLoginId = mutableStateOf("")
    val userLoginId: State<String> = _userLoginId

    private val _userPassword = mutableStateOf("")
    val userPassword: State<String> = _userPassword

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EnteredUserLoginId -> {
//                Log.d("Login", "Change Login ID : ${event.value}")
                _userLoginId.value = event.value
            }
            is LoginEvent.EnteredUserPassword -> {
                _userPassword.value = event.value
            }

            is LoginEvent.Login -> {
                viewModelScope.launch {
                    Log.d("Login", "userId: ${_userLoginId.value}")
                    loginUseCase.excute(
                        LoginRequest(
                            userLoginId = _userLoginId.value,
                            userPassword = _userPassword.value
                        )
                    ).catch { e ->
                        Log.d("Login Error", "${e.message}")
                    }.collectLatest { data ->
                        Log.d("Login Success", "Success")
                        // 토큰 저장 해야함
                        _userState.value = _userState.value.copy(token = data.accessToken, isLoggedIn = true)
                    }
                }
            }
        }
    }
}