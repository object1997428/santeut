package com.santeut.data.repository

import com.santeut.data.apiservice.MountainApiService
import javax.inject.Inject

class MountainRepositoryImpl @Inject constructor(
    private val mountainApiService: MountainApiService
) {
}