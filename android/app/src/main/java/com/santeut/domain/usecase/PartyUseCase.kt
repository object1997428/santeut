package com.santeut.domain.usecase

import com.santeut.data.model.request.CreatePartyRequest
import com.santeut.data.model.response.PartyResponse
import com.santeut.data.repository.PartyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PartyUseCase @Inject constructor(
    private val partyRepository: PartyRepository
) {

    suspend fun getPartyList(
        guildId: Int?,
        name: String?,
        start: String?,
        end: String?
    ): List<PartyResponse> = partyRepository.getPartyList(guildId, name, start, end)

    suspend fun createParty(createPartyRequest: CreatePartyRequest): Flow<Unit> =
        partyRepository.createParty(createPartyRequest)
}