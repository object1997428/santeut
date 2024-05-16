package com.santeut.data.model.response

import com.google.gson.annotations.SerializedName

data class AllcourseResponse (
    @SerializedName("course") val course: List<CourseDetailRespnse>
)

data class CourseDetailRespnse(
    @SerializedName("courseId") val courseId: Long,
    @SerializedName("distance") val distance: Double,
    @SerializedName("locationDataList") val locationDataList: List<LocationDataRespnse>
)

data class LocationDataRespnse(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double
)