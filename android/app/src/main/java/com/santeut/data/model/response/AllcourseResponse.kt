package com.santeut.data.model.response

import com.google.gson.annotations.SerializedName

data class AllCourseResponse(
    @SerializedName("course") val course: List<CourseDetailResponse>
)

data class CourseDetailResponse(
    @SerializedName("courseId") val courseId: Int,
    @SerializedName("distance") val distance: Double,
    @SerializedName("locationDataList") val locationDataList: List<LocationDataResponse>
) {
    val validLocationDataList: List<LocationDataResponse>
        get() = locationDataList.filter { !it.lat.isNaN() && !it.lng.isNaN() }
}

data class LocationDataResponse(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double
)