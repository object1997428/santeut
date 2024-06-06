/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.santeut

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Wearable
import com.santeut.ui.HealthDataViewModel
import com.santeut.ui.HealthDataViewModelFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val dataClient by lazy { Wearable.getDataClient(this) }
    private val messageClient by lazy { Wearable.getMessageClient(this) }
    private val capabilityClient by lazy { Wearable.getCapabilityClient(this) }

    private val healthDataViewModel: HealthDataViewModel by viewModels {
        HealthDataViewModelFactory(
            application = application,
            dataClient = Wearable.getDataClient(this),
            messageClient = Wearable.getMessageClient(this),
            capabilityClient = Wearable.getCapabilityClient(this)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            SanteutApp(
                healthDataViewModel = healthDataViewModel,
                onFinishActivity = {
                    this.finish()
                },
                onMoveTaskBack = {
                    moveTaskToBack(true)
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        dataClient.addListener(healthDataViewModel)
        messageClient.addListener(healthDataViewModel)
        capabilityClient.addListener(
            healthDataViewModel,
            Uri.parse("wear://"),
            CapabilityClient.FILTER_REACHABLE
        )
    }

    override fun onPause() {
        super.onPause()
        dataClient.removeListener(healthDataViewModel)
        messageClient.removeListener(healthDataViewModel)
        capabilityClient.removeListener(healthDataViewModel)
    }
}