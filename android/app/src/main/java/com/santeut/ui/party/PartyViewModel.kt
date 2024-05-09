package com.santeut.ui.party

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santeut.data.model.request.CreatePartyRequest
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

    fun createParty(
        schedule: String,
        partyName: String,
        mountainId: String,
        mountainName: String,
        maxPeople: String,
        guildId: String?,
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
            } catch (e:Exception){
                _error.value = "파티 생성 실패: ${e.message}"
                _partyCreationSuccess.value = false
            }
        }
    }

}