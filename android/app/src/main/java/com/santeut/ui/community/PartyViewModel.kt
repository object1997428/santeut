package com.santeut.ui.community

import androidx.lifecycle.ViewModel
import com.santeut.domain.usecase.PartyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PartyViewModel @Inject constructor(
    private val partyUseCase: PartyUseCase
): ViewModel() {
}