package com.santeut.data.repository

import com.santeut.data.apiservice.CommonApiService
import javax.inject.Inject

class CommonRepositoryImpl @Inject constructor(
    private val commonApiService: CommonApiService
) : CommonRepository {
}