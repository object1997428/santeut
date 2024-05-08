package com.santeut.ui.SummaryState

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
        healthServicesRepository.startExercise()
        _state.value = true
    }

    fun endExercise() {
        healthServicesRepository.endExercise()
        _state.value = false
    }
}