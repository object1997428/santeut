package com.santeut.data.apiservice

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.request.CreatePartyRequest
import com.santeut.data.model.response.ChatResponse
import com.santeut.data.model.response.ChatRoomResponse
import com.santeut.data.model.response.MyPartyListResponse
import com.santeut.data.model.response.MyRecordListResponse
import com.santeut.data.model.response.MyScheduleList
import com.santeut.data.model.response.PartyListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PartyApiService {

    @GET("/api/party/")
    suspend fun getPartyList(
        @Query("guild") guild: Int?,
        @Query("name") name: String?,
        @Query("start") start: String?,
        @Query("end") end: String?
    ): CustomResponse<PartyListResponse>

    @GET("/api/party/user")
    suspend fun getMyPartyList(
        @Query("date") date: String?,
        @Query("includeEnd") includeEnd: Boolean,
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): CustomResponse<MyPartyListResponse>

    @POST("/api/party/")
    suspend fun createParty(
        @Body createPartyRequest: CreatePartyRequest
    ): CustomResponse<Unit>

    @GET("/api/party/user/record")
    suspend fun getMyRecordList(): CustomResponse<MyRecordListResponse>

    @GET("/api/party/user/schedule")
    suspend fun getMyScheduleList(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): CustomResponse<MyScheduleList>

    @GET("/api/party/chat")
    suspend fun getMyChatList(): CustomResponse<ChatResponse>

    @GET("/api/party/chat/{partyId}")
    suspend fun getChatMessageList(
        @Path("partyId") partyId: Int
    ): CustomResponse<ChatRoomResponse>

}