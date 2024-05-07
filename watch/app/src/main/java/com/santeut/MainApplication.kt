package com.santeut

import android.app.Application
import com.santeut.data.HealthServicesRepository

const val PERMISSION = android.Manifest.permission.BODY_SENSORS

class MainApplication : Application() {
    val healthServicesRepository by lazy { HealthServicesRepository(this) }
}