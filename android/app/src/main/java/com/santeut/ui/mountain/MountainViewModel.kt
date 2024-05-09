package com.santeut.ui.mountain

import androidx.lifecycle.ViewModel
import com.santeut.domain.usecase.MountainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MountainViewModel @Inject constructor(
    private val mountainUseCase: MountainUseCase
) : ViewModel() {
}