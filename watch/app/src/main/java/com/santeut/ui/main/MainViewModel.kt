package com.santeut.ui.main

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santeut.data.HealthServicesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val healthServicesRepository: HealthServicesRepository
) : ViewModel() {

    private val _state = mutableStateOf(false)
    val state = _state

    init {
        viewModelScope.launch {
            healthServicesRepository.prepareExercise().let {
                Log.d("Prepare Success", "Prepare Success")
            }
        }
    }

    fun startExercise() {
        healthServicesRepository.startExercise()
        _state.value = true
    }

    fun endExercise() {
        healthServicesRepository.endExercise()
        _state.value = false
    }
}