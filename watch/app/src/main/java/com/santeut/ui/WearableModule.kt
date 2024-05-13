package com.santeut.ui

import android.content.Context
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Wearable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @Provides
    fun provideDataClient(@ApplicationContext context: Context): DataClient {
        return Wearable.getDataClient(context)
    }

    @Provides
    fun provideMessageClient(@ApplicationContext context: Context): MessageClient {
        return Wearable.getMessageClient(context)
    }

    @Provides
    fun provideCapabilityClient(@ApplicationContext context: Context): CapabilityClient {
        return Wearable.getCapabilityClient(context)
    }
}
