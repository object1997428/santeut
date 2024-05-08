package com.santeut.domain.usecase

import com.santeut.data.repository.PartyRepository
import javax.inject.Inject

class PartyUseCase @Inject constructor(
    private val partyRepository: PartyRepository
) {
}