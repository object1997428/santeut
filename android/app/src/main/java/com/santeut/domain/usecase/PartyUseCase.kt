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
        includeEnd: Boolean
    ): List<MyPartyResponse> = partyRepository.getMyPartyList(date, includeEnd)

    suspend fun createParty(createPartyRequest: CreatePartyRequest): Flow<Unit> =
        partyRepository.createParty(createPartyRequest)

    suspend fun getMyRecordList(): List<MyRecordResponse> = partyRepository.getMyRecordList()

    suspend fun getMyScheduleList(year: Int, month: Int): List<String> =
        partyRepository.getMyScheduleList(year, month)
}