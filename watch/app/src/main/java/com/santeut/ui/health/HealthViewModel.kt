package com.santeut.ui.health

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santeut.data.HealthServicesRepository
import com.santeut.data.ServiceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HealthViewModel @Inject constructor(
    private val healthServicesRepository: HealthServicesRepository
) : ViewModel() {

    val uiState: StateFlow<HealthScreenState> = healthServicesRepository.serviceState.map {
        HealthScreenState(
            hasExerciseCapabilities = healthServicesRepository.hasExerciseCapability(),
            isTrackingAnotherExercise = healthServicesRepository.isTrackingExerciseInAnotherApp(),
            serviceState = it,
            exerciseState = (it as? ServiceState.Connected)?.exerciseServiceState
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(3_000),
        healthServicesRepository.serviceState.value.let {
            HealthScreenState(
                true,
                false,
                it,
                (it as? ServiceState.Connected)?.exerciseServiceState
            )
        }
    )
}
