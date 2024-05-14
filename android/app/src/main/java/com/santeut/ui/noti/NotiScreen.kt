package com.santeut.ui.noti

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.EmojiSupportMatch
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.hilt.navigation.compose.hiltViewModel
import com.santeut.data.model.response.NotificationResponse
import com.santeut.ui.community.CommonViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotiScreen(commonViewModel: CommonViewModel = hiltViewModel()) {

    val notiList by commonViewModel.notiList.observeAsState(emptyList())

    LaunchedEffect(key1 = null) {
        commonViewModel.getNotificationList()
    }

    Scaffold {
        if (notiList.isNotEmpty()) {
            LazyColumn {
                items(notiList) { noti ->
                    NotiMessage(noti)
                }
            }
        }
    }
}

@Composable
fun NotiMessage(noti: NotificationResponse) {

    Column {

        // 이모지
        // 1. 게시글에대한 댓글(C)
        // 2. 게시글에대한 좋아요(L)
        // 3. 동호회 승인(G)
        // 4. 소모임장이 등산 시작을 누름(P)
        // 5. 등산(H)
        val emoji = when (noti.referenceType) {
            'C' -> "💬"
            'L' -> "💗"
            'G' -> "✔"
            'P' -> "⛰"
            'H' -> "⛰"
            else -> "▪"
        }

        Text(
            text = "$emoji ${noti.alarmTitle}",
            style = TextStyle(
                platformStyle = PlatformTextStyle(
                    emojiSupportMatch = EmojiSupportMatch.None
                )
            )
        )
        Text(text = noti.alarmContent)
    }
}