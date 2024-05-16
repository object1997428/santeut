package com.santeut.data.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.santeut.data.model.response.LoginResponse

class SharedPreferencesUtil(context: Context) {
    private val sharedPreferencesName = "store_preference"
    private val preferences: SharedPreferences =
        context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)

    fun saveToken(data: LoginResponse) {
        val editor = preferences.edit()
        editor.putString("accessToken", data.accessToken)
        editor.putString("refreshToken", data.refreshToken)
        editor.apply()

        Log.d("SharedPreference Save Token", "")
    }

    fun getAccessToken(): String? {
        return preferences.getString("accessToken", null)
    }
    fun getRefreshToken(): String? {
        return preferences.getString("refreshToken", null)
    }

    fun saveFcmToken(fcmToken: String) {
        val editor = preferences.edit()
        editor.putString("fcmToken", fcmToken)
        editor.apply()
    }

    fun getFcmToken(): String? {
        return preferences.getString("fcmToken", null)
    }
}
