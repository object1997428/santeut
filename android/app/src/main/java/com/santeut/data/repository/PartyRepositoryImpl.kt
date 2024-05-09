package com.santeut.data.repository

import com.santeut.data.apiservice.PartyApiService
import com.santeut.data.model.request.CreatePartyRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PartyRepositoryImpl @Inject constructor(
    private val partyApiService: PartyApiService
) : PartyRepository {
    override suspend fun createParty(createPartyRequest: CreatePartyRequest): Flow<Unit> = flow {
        val response = partyApiService.createParty(createPartyRequest)
        if(response.status=="201"){
            emit(response.data)
        }
    }
}