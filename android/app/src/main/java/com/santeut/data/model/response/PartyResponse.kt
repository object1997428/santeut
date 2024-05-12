package com.santeut.data.model.response

import com.google.gson.annotations.SerializedName


data class PartyListResponse(

    @SerializedName("content") val partyList: List<PartyResponse>,

    @SerializedName("page") val page: Int,
    @SerializedName("totalPage") val totalPage: Int,
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("size") val size: Int,
    @SerializedName("sorted") val sorted: Boolean,  // 오름차순으로 정렬
    @SerializedName("asc") val asc: Boolean,
    @SerializedName("filtered") val filtered: Boolean,
    @SerializedName("first") val first: Boolean,    // 첫 번째 페이지면 true
    @SerializedName("last") val last: Boolean  // 마지막 페이지면 true

)

data class PartyResponse(

    @SerializedName("partyId") val partyId: Int,
    @SerializedName("partyName") val partyName: String,
    @SerializedName("guildName") val guildName: String,
    @SerializedName("schedule") val schedule: String,
    @SerializedName("mountainName") val mountainName: String,
    @SerializedName("place") val place: String,
    @SerializedName("maxPeople") val maxPeople: Int,
    @SerializedName("curPeople") val curPeople: Int,
    @SerializedName("owner") val owner: String,
    @SerializedName("status") val status: String,
    @SerializedName("isMember") val isMember: Boolean

)

data class MyPartyListResponse(

    @SerializedName("content") val partyList: List<MyPartyResponse>,

    @SerializedName("page") val page: Int,
    @SerializedName("totalPage") val totalPage: Int,
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("size") val size: Int,
    @SerializedName("sorted") val sorted: Boolean,  // 오름차순으로 정렬
    @SerializedName("asc") val asc: Boolean,
    @SerializedName("filtered") val filtered: Boolean,
    @SerializedName("first") val first: Boolean,    // 첫 번째 페이지면 true
    @SerializedName("last") val last: Boolean  // 마지막 페이지면 true

)

data class MyPartyResponse(
    @SerializedName("partyId") val partyId: Int,
    @SerializedName("partyUserId") val partyUserId: Int,
    @SerializedName("partyName") val partyName: String,
    @SerializedName("guildName") val guildName: String,
    @SerializedName("schedule") val schedule: String,
    @SerializedName("mountainName") val mountainName: String,
    @SerializedName("place") val place: String,
    @SerializedName("maxPeople") val maxPeople: Int,
    @SerializedName("curPeople") val curPeople: Int,
    @SerializedName("owner") val owner: String,
    @SerializedName("status") val status: String,
)

data class MyRecordListResponse(
    @SerializedName("content") val recordList: List<MyRecordResponse>,

    @SerializedName("page") val page: Int,
    @SerializedName("totalPage") val totalPage: Int,
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("size") val size: Int,
    @SerializedName("sorted") val sorted: Boolean,  // 오름차순으로 정렬
    @SerializedName("asc") val asc: Boolean,
    @SerializedName("filtered") val filtered: Boolean,
    @SerializedName("first") val first: Boolean,    // 첫 번째 페이지면 true
    @SerializedName("last") val last: Boolean  // 마지막 페이지면 true
)

data class MyRecordResponse(
    @SerializedName("partyUserId") val partyUserId: Int,
    @SerializedName("partyName") val partyName: String,
    @SerializedName("guildName") val guildName: String,
    @SerializedName("mountainName") val mountainName: String,
    @SerializedName("schedule") val schedule: String,
    @SerializedName("distance") val distance: Double?,
    @SerializedName("height") val height: Int?,
    @SerializedName("duration") val duration: Int?
)
