package com.santeut.domain.usecase

import com.santeut.data.model.request.CreatePartyRequest
import com.santeut.data.model.response.MyPartyResponse
import com.santeut.data.model.response.MyRecordResponse
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

    suspend fun getMyPartyList(
        date: String?,
        includeEnd: Boolean,
        page: Int?,
        size: Int?
    ): List<MyPartyResponse> = partyRepository.getMyPartyList(date, includeEnd, page, size)

    suspend fun createParty(createPartyRequest: CreatePartyRequest): Flow<Unit> =
        partyRepository.createParty(createPartyRequest)

    suspend fun getMyRecordList(): List<MyRecordResponse> = partyRepository.getMyRecordList()
}