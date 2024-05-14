package com.santeut.data.repository

import android.util.Log
import com.santeut.data.apiservice.PartyApiService
import com.santeut.data.model.request.CreatePartyRequest
import com.santeut.data.model.response.MyPartyResponse
import com.santeut.data.model.response.PartyListResponse
import com.santeut.data.model.response.PartyResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PartyRepositoryImpl @Inject constructor(
    private val partyApiService: PartyApiService
) : PartyRepository {

    override suspend fun getPartyList(
        guildId: Int?,
        name: String?,
        start: String?,
        end: String?
    ): List<PartyResponse> {
        return try {
            val response = partyApiService.getPartyList(guildId, name, start, end)
            if (response.status == "200") {
                response.data.partyList ?: emptyList()
            } else {
                Log.e("PartyRepository", "소모임 목록 조회 실패: ${response.status} - ${response.data}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("PartyRepository", "소모임 목록 조회 실패: ${e.message}")
            throw e
        }
    }

    override suspend fun getMyPartyList(
        date: String?,
        includeEnd: Boolean
    ): List<MyPartyResponse> {
        return try {
            val response = partyApiService.getMyPartyList(date, includeEnd)
            if (response.status == "200") {
                response.data.partyList ?: emptyList()
            } else {
                Log.e("PartyRepository", "내 소모임 목록 조회 실패: ${response.status} - ${response.data}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("PartyRepository", "내 소모임 목록 조회 실패: ${e.message}")
            throw e
        }
    }


    override suspend fun createParty(createPartyRequest: CreatePartyRequest): Flow<Unit> = flow {
        val response = partyApiService.createParty(createPartyRequest)
        if (response.status == "201") {
            emit(response.data)
        }
    }
}