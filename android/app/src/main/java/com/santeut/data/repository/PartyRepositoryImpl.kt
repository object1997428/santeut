package com.santeut.data.repository

import android.util.Log
import com.santeut.data.apiservice.PartyApiService
import com.santeut.data.model.request.CreatePartyRequest
import com.santeut.data.model.request.PartyIdRequest
import com.santeut.data.model.response.ChatMessage
import com.santeut.data.model.response.ChatRoomInfo
import com.santeut.data.model.response.MyPartyResponse
import com.santeut.data.model.response.MyRecordResponse
import com.santeut.data.model.response.PartyResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PartyRepositoryImpl @Inject constructor(
    private val partyApiService: PartyApiService
) : PartyRepository {

    override suspend fun getPartyList(
        guildId: Int?,
        name: String?,
        start: String?,
        end: String?
    ): List<PartyResponse> {
        return try {
            val response = partyApiService.getPartyList(guildId, name, start, end)
            if (response.status == "200") {
                response.data.partyList ?: emptyList()
            } else {
                Log.e("PartyRepository", "소모임 목록 조회 실패: ${response.status} - ${response.data}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("PartyRepository", "소모임 목록 조회 실패: ${e.message}")
            throw e
        }
    }

    override suspend fun getParty(partyId: Int): PartyResponse {
        return try {
            val response = partyApiService.getParty(partyId)
            if (response.status == "200") {
                response.data
            } else {
                throw Exception("소모임 조회 실패: ${response.status} ${response.data}")
            }
        } catch (e: Exception) {
            Log.e("PartyRepository", "소모임 조회 실패: ${e.message}")
            throw e
        }
    }


    override suspend fun getMyPartyList(
        date: String?,
        includeEnd: Boolean,
        page: Int?,
        size: Int?
    ): List<MyPartyResponse> {
        return try {
            val response = partyApiService.getMyPartyList(date, includeEnd, page, size)
            if (response.status == "200") {
                response.data.partyList ?: emptyList()
            } else {
                Log.e(
                    "PartyRepository",
                    "내 소모임 목록 조회 실패: ${response.status} - ${response.data}"
                )
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("PartyRepository", "내 소모임 목록 조회 실패: ${e.message}")
            throw e
        }
    }


    override suspend fun createParty(createPartyRequest: CreatePartyRequest): Flow<Unit> =
        flow {
            val response = partyApiService.createParty(createPartyRequest)
            if (response.status == "201") {
                emit(response.data)
            }
        }

    override suspend fun deleteParty(partyId: Int): Flow<Unit> = flow {
        val response = partyApiService.deleteParty(PartyIdRequest(partyId))
        if (response.status == "200") {
            emit(response.data)
        }
    }

    override suspend fun joinParty(partyId: Int): Flow<Unit> = flow {
        val response = partyApiService.joinParty(PartyIdRequest(partyId))
        if (response.status == "200") {
            emit(response.data)
        }
    }

    override suspend fun quitParty(partyId: Int): Flow<Unit> = flow {
        val response = partyApiService.quitParty(partyId)
        if (response.status == "200") {
            emit(response.data)
        }
    }


    override suspend fun getMyRecordList(): List<MyRecordResponse> {
        return try {
            val response = partyApiService.getMyRecordList()
            if (response.status == "200") {
                response.data.recordList ?: emptyList()
            } else {
                Log.e(
                    "PartyRepository",
                    "내 산행 기록 조회 실패: ${response.status} - ${response.data}"
                )
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("PartyRepository", "내 산행 기록 조회 실패: ${e.message}")
            throw e
        }
    }

    override suspend fun getMyScheduleList(year: Int, month: Int): List<String> {
        return try {
            val response = partyApiService.getMyScheduleList(year, month)
            if (response.status == "200") {
                response.data.date ?: emptyList()
            } else {
                Log.e(
                    "PartyRepository",
                    "날짜별 소모임 조회 실패: ${response.status} - ${response.data}"
                )
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("PartyRepository", "날짜별 소모임 조회 실패: ${e.message}")
            throw e
        }
    }

    override suspend fun getChatList(): List<ChatRoomInfo> {
        return try {
            val response = partyApiService.getMyChatList()
            if (response.status == "200") {
                response.data.chatRoomList ?: emptyList()
            } else {
                Log.e(
                    "ChatRepository",
                    "채팅방 목록 조회 실패: ${response.status} - ${response.data}"
                )
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("ChatRepository", "Network error while fetching get: ${e.message}", e)
            throw e
        }

    }

    override suspend fun getChatMessageList(partyId: Int): MutableList<ChatMessage> {
        return try {
            val response = partyApiService.getChatMessageList(partyId)
            if (response.status == "200") {
                response.data.chatMessageList.toMutableList() ?: mutableListOf()
            } else {
                Log.e(
                    "ChatRepository",
                    "채팅방 대화내용 조회 실패: ${response.status} - ${response.data}"
                )
                mutableListOf()
            }
        } catch (e: Exception) {
            Log.e("ChatRepository", "Network error while fetching get: ${e.message}", e)
            throw e
        }
    }
}