package com.santeut.data.repository

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.response.AllcourseResponse
import com.santeut.data.model.response.CourseDetailRespnse
import com.santeut.data.model.response.HikingCourseResponse
import com.santeut.data.model.response.MountainDetailResponse
import com.santeut.data.model.response.MountainListResponse
import com.santeut.data.model.response.MountainResponse
import retrofit2.Call

interface MountainRepository {


    suspend fun getAllCourses(mountainId: Int) : List<CourseDetailRespnse>
    suspend fun popularMountain(): List<MountainResponse>
    suspend fun searchMountain(name: String, region: String?): List<MountainResponse>
    suspend fun mountainDetail(mountainId: Int): MountainDetailResponse
    suspend fun getHikingCourseList(mountainId: Int): List<HikingCourseResponse>
}


