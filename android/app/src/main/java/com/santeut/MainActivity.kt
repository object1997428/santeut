package com.santeut

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Wearable
import com.google.firebase.messaging.FirebaseMessaging
import com.santeut.designsystem.theme.SanteutTheme
import com.santeut.ui.wearable.WearableViewModel
import com.santeut.ui.wearable.WearableViewModelFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var isCameraPermissionGranted = false
    private var isForegroundServiceGranted = false
    private var isPostNotificationsPermissionGranted = false
<<<<<<< HEAD
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){
        isGranted ->
        if (isGranted) {
            Log.i("kilo", "Permission granted")
        }
        else {
            Log.i("kilo", "Permission denied")
        }
    }
=======

    private val dataClient by lazy { Wearable.getDataClient(this) }
    private val messageClient by lazy { Wearable.getMessageClient(this) }
    private val capabilityClient by lazy { Wearable.getCapabilityClient(this) }

    private val wearableViewModel: WearableViewModel by viewModels {
        WearableViewModelFactory(
            dataClient = Wearable.getDataClient(this),
            messageClient = Wearable.getMessageClient(this),
            capabilityClient = Wearable.getCapabilityClient(this)
        )
    }

>>>>>>> d6cc9bbc9799cdc6845814115e0a49e707e6e65e
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SanteutTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SanteutApp(
                        wearableViewModel = wearableViewModel
                    )
                }
            }
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions ->
            isCameraPermissionGranted = permissions[android.Manifest.permission.CAMERA] ?: isCameraPermissionGranted
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                isPostNotificationsPermissionGranted = permissions[android.Manifest.permission.POST_NOTIFICATIONS] ?: isPostNotificationsPermissionGranted
            }
        }

        requestPermission()

        getFCMToken()
    }

    override fun onResume() {
        super.onResume()
        dataClient.addListener(wearableViewModel)
        messageClient.addListener(wearableViewModel)
        capabilityClient.addListener(
            wearableViewModel,
            Uri.parse("wear://"),
            CapabilityClient.FILTER_REACHABLE
        )
    }

    override fun onPause() {
        super.onPause()
        dataClient.removeListener(wearableViewModel)
        messageClient.removeListener(wearableViewModel)
        capabilityClient.removeListener(wearableViewModel)
    }

    private fun requestPermission() {
        val permissionRequest : MutableList<String> = ArrayList()

        isCameraPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if(!isCameraPermissionGranted){
            permissionRequest.add(android.Manifest.permission.CAMERA)
            Log.d("Permission Check", "Need Camera Permission")
        }

        isForegroundServiceGranted = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.FOREGROUND_SERVICE
        ) == PackageManager.PERMISSION_GRANTED

        if(!isForegroundServiceGranted){
            permissionRequest.add(android.Manifest.permission.FOREGROUND_SERVICE)
            Log.d("Permission Check", "Need Camera Permission")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            isPostNotificationsPermissionGranted = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if(!isPostNotificationsPermissionGranted){
                permissionRequest.add(android.Manifest.permission.POST_NOTIFICATIONS)
                permissionRequest.add(android.Manifest.permission.CAMERA)
                Log.d("Permission Check", "Need Post Notifications Permission")
            }
        }

        if(permissionRequest.isNotEmpty()){
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }
    }

    fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Firebase", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result
            Log.d("FCM My Token", token.toString())
            MainApplication.sharedPreferencesUtil.saveFcmToken(token)
        })
    }
}