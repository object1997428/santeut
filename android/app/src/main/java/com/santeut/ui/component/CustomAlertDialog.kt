package com.santeut.ui.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomAlertDialog(
    title: String,
    text: String,
    onConfirmButton: () -> Unit,
    onDismissButton: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = title)
        },
        text = {
            Text(text = text)
        },
        confirmButton = {
            TextButton(
                onClick = {}
            ) {
                Text("확인")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {}
            ) {
                Text("취소")
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}

@Preview
@Composable
fun CustomAlertDialogPreview() {
    CustomAlertDialog(
        title = "오류",
        text = "로그인을 실패하였습니다.",
        onConfirmButton = {},
        onDismissButton = {}
    )
}
