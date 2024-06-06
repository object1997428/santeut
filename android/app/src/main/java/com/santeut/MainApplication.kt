package com.santeut

import android.app.Application
import com.santeut.data.util.SharedPreferencesUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    companion object {
        lateinit var sharedPreferencesUtil: SharedPreferencesUtil
    }

    override fun onCreate() {
        super.onCreate()

        sharedPreferencesUtil = SharedPreferencesUtil(this)
    }
}