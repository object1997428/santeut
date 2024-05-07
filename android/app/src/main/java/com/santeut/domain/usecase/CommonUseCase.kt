package com.santeut.domain.usecase

import com.santeut.data.repository.CommonRepository
import javax.inject.Inject

class CommonUseCase @Inject constructor(
    private val commonRepository: CommonRepository
){
}