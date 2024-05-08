package com.santeut.data.repository

import com.santeut.data.apiservice.PartyApiService
import javax.inject.Inject

class PartyRepositoryImpl @Inject constructor(
    private val partyApiService: PartyApiService
) : PartyRepository {
}