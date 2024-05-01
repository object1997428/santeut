package com.ssafy.santeut.ui.login

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.santeut.data.model.request.LoginRequest
import com.ssafy.santeut.domain.usecase.LoginUseCase
import com.ssafy.santeut.ui.landing.UserState
<<<<<<< HEAD
import com.ssafy.santeut.ui.landing.UserViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
=======
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
>>>>>>> 070aae5b7e21058a8d65a52b920d7911e350f8a5
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

    private val _uiEvent = MutableStateFlow<LoginUiEvent>(LoginUiEvent.Idle)
    val uiEvent: StateFlow<LoginUiEvent> = _uiEvent

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EnteredUserLoginId -> {
<<<<<<< HEAD
//                Log.d("Login", "Change Login ID : ${event.value}")
                _userLoginId.value = event.value
            }
=======
                _userLoginId.value = event.value
            }

>>>>>>> 070aae5b7e21058a8d65a52b920d7911e350f8a5
            is LoginEvent.EnteredUserPassword -> {
                _userPassword.value = event.value
            }

            is LoginEvent.Login -> {
<<<<<<< HEAD
                viewModelScope.launch {
                    Log.d("Login", "userId: ${_userLoginId.value}")
=======
                viewModelScope.launch(Dispatchers.IO) {
                    Log.d("Login ViewModel", "이벤트 발생")
>>>>>>> 070aae5b7e21058a8d65a52b920d7911e350f8a5
                    loginUseCase.excute(
                        LoginRequest(
                            userLoginId = _userLoginId.value,
                            userPassword = _userPassword.value
                        )
                    ).catch { e ->
                        Log.d("Login Error", "${e.message}")
                        _uiEvent.value = LoginUiEvent.Login(false);
                    }.collectLatest { data ->
                        Log.d("Login Success", "Success")
                        // 토큰 저장 해야함
<<<<<<< HEAD
                        _userState.value = _userState.value.copy(token = data.accessToken, isLoggedIn = true)
=======
                        _userState.value =
                            _userState.value.copy(token = data.accessToken, isLoggedIn = true)
>>>>>>> 070aae5b7e21058a8d65a52b920d7911e350f8a5
                        _uiEvent.value = LoginUiEvent.Login(true);
                    }
                }
            }
        }
    }

    @Stable
    sealed interface LoginUiEvent {
        @Immutable
        data object Idle : LoginUiEvent
<<<<<<< HEAD
=======

>>>>>>> 070aae5b7e21058a8d65a52b920d7911e350f8a5
        @Immutable
        data class Login(val success: Boolean) : LoginUiEvent
    }
}