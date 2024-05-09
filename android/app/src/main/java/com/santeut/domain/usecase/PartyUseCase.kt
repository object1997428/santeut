package com.santeut.domain.usecase

import com.santeut.data.model.request.CreatePartyRequest
import com.santeut.data.repository.PartyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PartyUseCase @Inject constructor(
    private val partyRepository: PartyRepository
) {
    suspend fun createParty(createPartyRequest: CreatePartyRequest): Flow<Unit> =
        partyRepository.createParty(createPartyRequest)
}