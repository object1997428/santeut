package com.santeut.data.repository

import com.santeut.data.model.request.CreatePartyRequest
import com.santeut.data.model.response.MyPartyResponse
import com.santeut.data.model.response.MyRecordResponse
import com.santeut.data.model.response.PartyResponse
import kotlinx.coroutines.flow.Flow

interface PartyRepository {

    suspend fun getPartyList(
        guildId: Int?,
        name: String?,
        start: String?,
        end: String?
    ): List<PartyResponse>

    suspend fun getMyPartyList(
        date: String?,
        includeEnd: Boolean
    ): List<MyPartyResponse>

    suspend fun createParty(createPartyRequest: CreatePartyRequest): Flow<Unit>

    suspend fun getMyRecordList(): List<MyRecordResponse>

    suspend fun getMyScheduleList(year: Int, month: Int): List<String>
}