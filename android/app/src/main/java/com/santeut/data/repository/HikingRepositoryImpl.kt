package com.santeut.data.repository

import com.santeut.data.apiservice.HikingApiService
import javax.inject.Inject

class HikingRepositoryImpl @Inject constructor(
    private val hikingApiService: HikingApiService
) : HikingRepository {
}