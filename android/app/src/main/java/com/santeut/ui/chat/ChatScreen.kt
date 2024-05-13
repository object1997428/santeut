package com.santeut.ui.chat

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.santeut.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.santeut.designsystem.theme.DarkGreen


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChatScreen(partyId: Int) {
    Scaffold(modifier = Modifier.fillMaxSize(),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Surface (
                    modifier = Modifier.weight(1f)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth(),
                        reverseLayout = true,
                        state = LazyListState(firstVisibleItemIndex = 0)
                    ) {
                        items(100) {
                            ChatMessage()
                        }
                    }
                }
                ChatBox()
            }
        }
    )
}

@Composable
fun ChatMessage() {
    Row(
        modifier = Modifier.padding(16.dp, 8.dp)
    ) {

        AsyncImage(
            model = "이미지" ?: R.drawable.logo,
            contentDescription = "프로필 이미지",
            modifier = Modifier
                .width(20.dp)
                .height(20.dp),
            contentScale = ContentScale.Crop
        )

        Column() {
            Text(
                "닉네임",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 4.dp)
            )
            Box(
                modifier = Modifier
//            .align(if (message.isFromMe) Alignment.End else Alignment.Start)
                    .clip(
                        RoundedCornerShape(
                            topStart = 48f,
                            topEnd = 48f,
                            bottomStart = 48f,
                            bottomEnd = 48f
//                    bottomStart = if (message.isFromMe) 48f else 0f,
//                    bottomEnd = if (message.isFromMe) 0f else 48f
                        )
                    )
                    .background(DarkGreen)
                    .padding(16.dp)
            ) {
                Text(
                    text = "메시지메시지메시지메시지메시지메시지메시지메시지메시지메시지메시지메시지메시지메시지메시지메시지메시지메시지",
                    color = Color.White,
                    softWrap = true
                )
            }
            Text(
                "yyyy-mm-dd hh:mm", color = Color.LightGray,
                modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp)
            )
        }

    }

}

@Composable
fun ChatBox() {
    var chatBoxValue by remember { mutableStateOf(TextFieldValue("")) }
    var chatBoxValueHasValue = chatBoxValue.text.isNotEmpty()

    OutlinedTextField(
        modifier = Modifier.padding(8.dp, 4.dp)
            .fillMaxWidth(),
        value = chatBoxValue,
        onValueChange = { chatBoxValue = it },
        shape = CircleShape,
        trailingIcon = {
            if(chatBoxValueHasValue) {
                IconButton (
                    onClick = {
                        // send
                        Log.d("SEND", chatBoxValue.text)
                        chatBoxValue = TextFieldValue("") // 채팅창 내용 초기화
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = null
                    )
                }
            }
        }
    )

}
