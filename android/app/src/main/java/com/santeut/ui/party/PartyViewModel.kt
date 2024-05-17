package com.santeut.ui.party

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santeut.data.model.request.CreatePartyRequest
import com.santeut.data.model.response.MountainDetailResponse
import com.santeut.data.model.response.MyPartyResponse
import com.santeut.data.model.response.MyRecordResponse
import com.santeut.data.model.response.PartyCourseResponse
import com.santeut.data.model.response.PartyResponse
import com.santeut.domain.usecase.PartyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartyViewModel @Inject constructor(
    private val partyUseCase: PartyUseCase
) : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _partyCreationSuccess = MutableLiveData<Boolean>()
    val partyCreationSuccess: LiveData<Boolean> = _partyCreationSuccess

    private val _partyList = MutableLiveData<List<PartyResponse>>()
    val partyList: LiveData<List<PartyResponse>> = _partyList

    private val _myPartyList = MutableLiveData<List<MyPartyResponse>>()
    val myPartyList: LiveData<List<MyPartyResponse>> = _myPartyList

    private val _myRecordList = MutableLiveData<List<MyRecordResponse>>()
    val myRecordList: LiveData<List<MyRecordResponse>> = _myRecordList

    private val _myScheduleList = MutableLiveData<List<String>>()
    val myScheduleList: LiveData<List<String>> = _myScheduleList

    private val _selectedCourseOfParty = MutableLiveData<PartyCourseResponse>()
    val selectedCourseOfParty: LiveData<PartyCourseResponse> = _selectedCourseOfParty

    fun getPartyList(
        guildId: Int?,
        name: String?,
        start: String?,
        end: String?
    ) {
        viewModelScope.launch {
            try {
                Log.d("PartyViewModel", _partyList.value?.size.toString())
                _partyList.postValue(partyUseCase.getPartyList(guildId, name, start, end))
                Log.d("PartyViewModel", _partyList.value?.size.toString())
            } catch (e: Exception) {
                _error.postValue("소모임 목록 조회 실패: ${e.message}")
            }
        }
    }

    fun getMyPartyList(
        date: String?,
        includeEnd: Boolean,
        page: Int?,
        size: Int?
    ) {
        viewModelScope.launch {
            try {
                Log.d("PartyViewModel", _myPartyList.value?.size.toString())
                _myPartyList.postValue(partyUseCase.getMyPartyList(date, includeEnd, page, size))
            } catch (e: Exception) {
                _error.postValue("내 소모임 목록 조회 실패: ${e.message}")
            }
        }
    }

    fun createParty(
        createPartyRequest: CreatePartyRequest
    ) {
        viewModelScope.launch {
            try {
                partyUseCase.createParty(createPartyRequest).collect {
                    _partyCreationSuccess.value = true
                }
            } catch (e: Exception) {
                _error.value = "파티 생성 실패: ${e.message}"
                _partyCreationSuccess.value = false
            }
        }
    }

    fun deleteParty(partyId: Int) {
        viewModelScope.launch {
            try {
                partyUseCase.deleteParty(partyId).collect {
                    Log.d("PartyViewModel", "소모임 삭제 성공")
                }
            } catch (e: Exception) {
                _error.value = "소모임 삭제 실패: ${e.message}"
            }
        }
    }

    fun joinParty(partyId: Int) {
        viewModelScope.launch {
            try {
                partyUseCase.joinParty(partyId).collect {
                    Log.d("PartyViewModel", "소모임 가입 성공")
                }
            } catch (e: Exception) {
                _error.value = "소모임 가입 실패: ${e.message}"
            }
        }
    }

    fun quitParty(partyId: Int) {
        viewModelScope.launch {
            try {
                partyUseCase.quitParty(partyId).collect {
                    Log.d("PartyViewModel", "소모임 탈퇴 성공")
                }
            } catch (e: Exception) {
                _error.value = "소모임 탈퇴 실패: ${e.message}"
            }
        }
    }


    fun getMyRecordList() {
        viewModelScope.launch {
            try {
                _myRecordList.postValue(partyUseCase.getMyRecordList())
                Log.d("PartyViewModel", "내 산행 목록 조회 시도")
            } catch (e: Exception) {
                _error.postValue("내 산행 목록 조회 실패: ${e.message}")
            }
        }
    }

    fun getMyScheduleList(year: Int, month: Int) {
        viewModelScope.launch {
            try {
                _myScheduleList.postValue(partyUseCase.getMyScheduleList(year, month))
                Log.d("PartyViewModel", "날짜별 소모임 조회 시도")
            } catch (e: Exception) {
                _error.postValue("날짜별 소모임 조회 실패: ${e.message}")
            }
        }
    }

    fun getSelectedCourseInfoOfParty(partyId:Int) {
        viewModelScope.launch {
            try {
                _selectedCourseOfParty.postValue(partyUseCase.getSelectedCourseOfParty(partyId))
                Log.d("PartyViewModel", "소모임 선택 등산로 정보 조회 시도")
            } catch (e:Exception) {
                _error.postValue("소모임 선택 등산로 정보 조회 실패: ${e.message}")
            }
        }
    }
}