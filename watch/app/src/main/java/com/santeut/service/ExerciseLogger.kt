package com.santeut.service

import android.util.Log

private const val TAG = "Santeut"

interface ExerciseLogger {
    fun error(message: String, throwable: Throwable? = null)
    fun log(message: String)
}

class AndroidLogExerciseLogger : ExerciseLogger {
    override fun error(message: String, throwable: Throwable?) {
        Log.e(TAG, message, throwable)
    }

    override fun log(message: String) {
        Log.i(TAG, message)
    }
}