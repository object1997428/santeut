package com.santeut.ui.main

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.santeut.data.HealthServicesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val healthServicesRepository: HealthServicesRepository
) : ViewModel() {

    private val _state = mutableStateOf(false)
    val state = _state

    fun startExercise() {
        Log.d("너 존재하니?", healthServicesRepository.toString())

        healthServicesRepository.startExercise()
        _state.value = true
    }
    fun endExercise() {
        healthServicesRepository.endExercise()
        _state.value = false
    }
}