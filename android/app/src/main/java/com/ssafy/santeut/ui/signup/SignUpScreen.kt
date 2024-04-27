package com.ssafy.santeut.ui.signup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.santeut.ui.login.LoginEvent

@Composable
fun SignUpScreen(
    onNavigateLogin: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val userLoginId = viewModel.userLoginId.value
    val userNickName = viewModel.userNickName.value
    val userPassword = viewModel.userPassword.value
    val userPassword2 = viewModel.userPassword2.value
    val userBirth = viewModel.userBirth.value
    val userGender = viewModel.userGender.value

    val signUpSuccess = viewModel.signUpSuccess.value

    LaunchedEffect(signUpSuccess) {
        if (signUpSuccess) {
            onNavigateLogin()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            Text(text = "회원가입 페이지")
            OutlinedTextField(
                value = userLoginId,
                label = { Text(text = "아이디") },
                placeholder = { Text(text = "아이디") },
                onValueChange = { newValue ->
                    viewModel.onEvent(SignUpEvent.EnteredUserLoginId(newValue))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                value = userNickName,
                label = { Text(text = "닉네임") },
                placeholder = { Text(text = "닉네임") },
                onValueChange = { newValue ->
                    viewModel.onEvent(SignUpEvent.EnteredUserNickName(newValue))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                value = userPassword,
                label = { Text(text = "비밀번호") },
                placeholder = { Text(text = "비밀번호") },
                onValueChange = { newValue ->
                    viewModel.onEvent(SignUpEvent.EnteredUserPassword(newValue))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            OutlinedTextField(
                value = userPassword2,
                label = { Text(text = "비밀번호 확인") },
                placeholder = { Text(text = "비밀번호 확인") },
                onValueChange = { newValue ->
                    viewModel.onEvent(SignUpEvent.EnteredUserPassword2(newValue))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            OutlinedTextField(
                value = userBirth,
                label = { Text(text = "생년월일") },
                placeholder = { Text(text = "생년월일") },
                onValueChange = { newValue ->
                    viewModel.onEvent(SignUpEvent.EnteredUserBirth(newValue))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
            Switch(
                checked = userGender,
                onCheckedChange = { newValue ->
                    viewModel.onEvent(SignUpEvent.EnteredUserGender(newValue))
                }
            )
            Button(
                onClick = {
                    viewModel.onEvent(SignUpEvent.SignUp)
                },
            ) {
                Text(text = "signup")
            }
            Button(
                onClick = {
                    onNavigateLogin()
                },
            ) {
                Text(text = "Login Page")
            }
        }
    }
}