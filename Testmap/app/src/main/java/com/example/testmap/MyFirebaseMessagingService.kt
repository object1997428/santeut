package com.example.testmap

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService()  {

    override fun onNewToken(token: String){
        super.onNewToken(token)
        Log.d("FCM 토큰", "FCM 등록토큰: " + token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d("FCM Response", "From: ${message.from}")
        Log.d("FCM Response", "data: ${message.data}")
    }

//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        Log.d("메세지", "From: ${remoteMessage.from}")
//
//        // 메시지 데이터가 있는 경우
//        remoteMessage.data.isNotEmpty().let {
//            Log.d("메세지", "Message data payload: " + remoteMessage.data)
//        }
//
//        // 메시지 알림이 있는 경우
//        remoteMessage.notification?.let {
//            Log.d("메세지", "Message Notification Body: ${it.body}")
//        }
//    }

}