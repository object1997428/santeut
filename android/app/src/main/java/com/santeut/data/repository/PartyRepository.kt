package com.santeut.data.repository

import com.santeut.data.model.request.CreatePartyRequest
import kotlinx.coroutines.flow.Flow

interface PartyRepository {
    suspend fun createParty(createPartyRequest: CreatePartyRequest): Flow<Unit>
}