package com.santeut.ui.party

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santeut.data.model.request.CreatePartyRequest
import com.santeut.data.model.response.MyPartyResponse
import com.santeut.data.model.response.PartyResponse
import com.santeut.domain.usecase.PartyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun getPartyList(
        guildId: Int?,
        name: String?,
        start: String?,
        end: String?
    ) {
        viewModelScope.launch{
            try {
                Log.d("PartyViewModel", _partyList.value?.size.toString())
                _partyList.value = partyUseCase.getPartyList(guildId, name, start, end)
            } catch (e:Exception){
                _error.value = "소모임 목록 조회 실패: ${e.message}"
            }
        }
    }

    fun getMyPartyList(
        date: String?,
        includeEnd: Boolean,
        page: Int?,
        size: Int?
    ){
        viewModelScope.launch{
            try {
                Log.d("PartyViewModel", _myPartyList.value?.size.toString())
                _myPartyList.value = partyUseCase.getMyPartyList(date, includeEnd, page, size)
            } catch (e:Exception){
                _error.value = "내 소모임 목록 조회 실패: ${e.message}"
            }
        }
    }

    fun createParty(
        schedule: String,
        partyName: String,
        mountainId: Int,
        mountainName: String,
        maxPeople: Int,
        guildId: Int?,
        place: String,
        selectedCourse: List<Int>
    ) {
        viewModelScope.launch {
            try {
                val createPartyRequest = CreatePartyRequest(
                    schedule,
                    partyName,
                    mountainId,
                    mountainName,
                    maxPeople,
                    guildId,
                    place,
                    selectedCourse
                )

                partyUseCase.createParty(createPartyRequest).collect {
                    _partyCreationSuccess.value = true
                }
            } catch (e: Exception) {
                _error.value = "파티 생성 실패: ${e.message}"
                _partyCreationSuccess.value = false
            }
        }
    }

}