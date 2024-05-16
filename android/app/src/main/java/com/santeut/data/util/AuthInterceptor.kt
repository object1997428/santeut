package com.santeut.data.util

import android.util.Log
import com.santeut.MainApplication
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        val token = MainApplication.sharedPreferencesUtil.getAccessToken()
        Log.d("Auth Interceptor", "$token")

        if(token != null){
            requestBuilder.addHeader(
                "Authorization",
                "Bearer $token"
            )
        }

        return chain.proceed(requestBuilder.build())
    }
}
