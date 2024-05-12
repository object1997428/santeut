package com.santeut.data.repository

import com.santeut.data.model.request.CreatePartyRequest
import com.santeut.data.model.response.MyPartyResponse
import com.santeut.data.model.response.MyRecordResponse
import com.santeut.data.model.response.PartyListResponse
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
        includeEnd: Boolean,
        page: Int?,
        size: Int?
    ): List<MyPartyResponse>

    suspend fun createParty(createPartyRequest: CreatePartyRequest): Flow<Unit>

    suspend fun getMyRecordList(): List<MyRecordResponse>
}