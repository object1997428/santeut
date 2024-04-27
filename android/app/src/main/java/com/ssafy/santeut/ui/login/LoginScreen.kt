package com.ssafy.santeut.ui.login

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(
    onNavigateSignUp: () -> Unit,
    onNavigateHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val userLoginId = viewModel.userLoginId.value
    val userPassword = viewModel.userPassword.value

    val userState = viewModel.userState.value

    LaunchedEffect(userState.isLoggedIn) {
        Log.d("LoginScreen", "${userState.isLoggedIn}")
        if (userState.isLoggedIn) {
            onNavigateHome()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { keyboardController?.hide() }
    ) {
        Column {
            Text(text = "로그인 페이지")
            OutlinedTextField(
                value = userLoginId,
                label = { Text(text = "아이디") },
                placeholder = { Text(text = "아이디") },
                onValueChange = { newValue ->
                    viewModel.onEvent(LoginEvent.EnteredUserLoginId(newValue))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                value = userPassword,
                label = { Text(text = "비밀번호") },
                placeholder = { Text(text = "비밀번호") },
                onValueChange = { newValue ->
                    viewModel.onEvent(LoginEvent.EnteredUserPassword(newValue))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.onEvent(LoginEvent.Login)
                        keyboardController?.hide()
                    }
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            Button(
                onClick = {
                    viewModel.onEvent(LoginEvent.Login)
                },
            ) {
                Text(text = "Login")
            }
            Button(
                onClick = {
                    onNavigateSignUp()
                },
            ) {
                Text(text = "SignUp Page")
            }
        }
    }
}